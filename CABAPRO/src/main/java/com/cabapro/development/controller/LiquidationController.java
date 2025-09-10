package com.cabapro.development.controller;

import com.cabapro.development.model.Assignment;
import com.cabapro.development.model.Match;
import com.cabapro.development.model.Ranking;
import com.cabapro.development.model.Referee;
import com.cabapro.development.model.Tournament;
import com.cabapro.development.repository.AssignmentRepository;
import com.cabapro.development.repository.RefereeRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

// iText (lowagie) — imports explícitos (¡nada de com.lowagie.text.*!)
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.Color;

@Controller
@RequestMapping("/admin/liquidations")
public class LiquidationController {

    private final AssignmentRepository assignmentRepo;
    private final RefereeRepository refereeRepo;

    public LiquidationController(AssignmentRepository assignmentRepo, RefereeRepository refereeRepo) {
        this.assignmentRepo = assignmentRepo;
        this.refereeRepo = refereeRepo;
    }

    @GetMapping
    public String index(@RequestParam(value = "period", required = false) String period, Model model) {
        YearMonth ym = (period == null || period.isBlank()) ? YearMonth.now() : YearMonth.parse(period);
        LocalDateTime start = ym.atDay(1).atStartOfDay();
        LocalDateTime end = ym.atEndOfMonth().atTime(23, 59, 59);

        List<Assignment> asgs = assignmentRepo.findByMatch_MatchDateBetween(start, end);

        Map<Long, Referee> refMap = refereeRepo
                .findAllById(asgs.stream().map(a -> a.getReferee().getId()).collect(Collectors.toSet()))
                .stream()
                .collect(Collectors.toMap(Referee::getId, Function.identity()));

        Map<Long, LiqRow> rows = new LinkedHashMap<>();
        for (Assignment a : asgs) {
            Long rid = a.getReferee().getId();
            LiqRow row = rows.computeIfAbsent(rid, k -> {
                Referee r = refMap.get(k);
                String email = (r != null) ? r.getEmail() : ("#" + k);
                return new LiqRow(k, email);
            });
            row.assignments++;
            row.total += computeAmount(a);
        }

        model.addAttribute("period", ym.toString());
        model.addAttribute("rows", new ArrayList<>(rows.values()));
        return "admin/liquidations/list";
    }

    @GetMapping("/export/{refereeId}")
    public void exportPdf(@PathVariable Long refereeId,
                          @RequestParam("period") @DateTimeFormat(pattern = "yyyy-MM") YearMonth period,
                          HttpServletResponse response) throws Exception {

        LocalDateTime start = period.atDay(1).atStartOfDay();
        LocalDateTime end = period.atEndOfMonth().atTime(23, 59, 59);

        Referee r = refereeRepo.findById(refereeId)
                .orElseThrow(() -> new IllegalArgumentException("Referee not found"));
        List<Assignment> list = assignmentRepo.findForRefereeInMonth(refereeId, start, end);

        double total = 0.0;

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=liquidation-" + r.getEmail() + "-" + period + ".pdf");

        Document doc = new Document(PageSize.A4, 36, 36, 36, 36);
        OutputStream os = response.getOutputStream();
        PdfWriter.getInstance(doc, os);
        doc.open();

        Font h1 = new Font(Font.HELVETICA, 16, Font.BOLD);
        Font th = new Font(Font.HELVETICA, 10, Font.BOLD);
        Font td = new Font(Font.HELVETICA, 10, Font.NORMAL);

        Paragraph title = new Paragraph("Monthly Liquidation", h1);
        title.setAlignment(Element.ALIGN_LEFT);
        doc.add(title);
        doc.add(new Paragraph("Referee: " + r.getEmail(), td));
        doc.add(new Paragraph("Period: " + period, td));
        doc.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(new float[]{3, 2, 2, 2});
        table.setWidthPercentage(100);

        addCell(table, "Match", th, true);
        addCell(table, "Ranking fee", th, true);
        addCell(table, "Tournament fee", th, true);
        addCell(table, "Total (row)", th, true);

        for (Assignment a : list) {
            double rankingFee = getRankingFee(a);
            double tournamentFee = getTournamentFee(a);
            double perAssign = getPerAssignmentFee(a);
            double rowTotal = rankingFee + tournamentFee + perAssign;
            total += rowTotal;

            String matchLabel = buildMatchLabel(a.getMatch());
            addCell(table, matchLabel, td, false);
            addCell(table, money(rankingFee), td, false);
            addCell(table, money(tournamentFee), td, false);
            addCell(table, money(rowTotal), td, false);
        }

        doc.add(table);
        doc.add(Chunk.NEWLINE);
        Paragraph grand = new Paragraph("Grand total: " + money(total), h1);
        grand.setAlignment(Element.ALIGN_RIGHT);
        doc.add(grand);

        doc.close();
        os.flush();
    }

    // ===== helpers =====

    private String buildMatchLabel(Match m) {
        if (m == null) return "-";
        String t = (m.getTournament() != null) ? m.getTournament().getName() : "-";
        String vs = (m.getHomeTeam() != null ? m.getHomeTeam() : "-") +
                " vs " + (m.getAwayTeam() != null ? m.getAwayTeam() : "-");
        return vs + " / " + t + " / " + (m.getMatchDate() != null ? m.getMatchDate() : "");
    }

    private double computeAmount(Assignment a) {
        return getRankingFee(a) + getTournamentFee(a) + getPerAssignmentFee(a);
    }

    private double getRankingFee(Assignment a) {
        Ranking rk = (a.getReferee() != null) ? a.getReferee().getRanking() : null;
        return rk != null ? rk.getFee() : 0.0;
    }

    private double getTournamentFee(Assignment a) {
        Tournament t = (a.getMatch() != null) ? a.getMatch().getTournament() : null;
        return t != null ? t.getBaseFee() : 0.0;
    }

    private double getPerAssignmentFee(Assignment a) {
        // Si tu entidad Assignment tiene getAssignmentFee(), úsalo; si no, 0
        try {
            var m = a.getClass().getMethod("getAssignmentFee");
            Object v = m.invoke(a);
            return (v instanceof Number) ? ((Number) v).doubleValue() : 0.0;
        } catch (Exception ignore) {
            return 0.0;
        }
    }

    private void addCell(PdfPTable t, String text, Font f, boolean header) {
        PdfPCell c = new PdfPCell(new Phrase(text, f));
        if (header) {
            c.setBackgroundColor(new Color(230, 230, 230));
        }
        c.setPadding(6f);
        t.addCell(c);
    }

    private String money(double v) {
        return String.format("$%.2f", v);
    }

    public static class LiqRow {
        private final Long refereeId;
        private final String email;
        private int assignments = 0;
        private double total = 0;

        public LiqRow(Long refereeId, String email) {
            this.refereeId = refereeId;
            this.email = email;
        }
        public Long getRefereeId() { return refereeId; }
        public String getEmail() { return email; }
        public int getAssignments() { return assignments; }
        public double getTotal() { return total; }
    }
}