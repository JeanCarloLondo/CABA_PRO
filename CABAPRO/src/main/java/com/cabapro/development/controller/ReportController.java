package com.cabapro.development.controller;

import com.cabapro.development.service.report.RankingReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

@Controller
public class ReportController {

    private final RankingReportService reportService;

    public ReportController(RankingReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/admin/rankings/report")
    public ResponseEntity<byte[]> exportRankings(@RequestParam String type) {
        byte[] bytes = reportService.generate(type);

        String ext = type.equals("pdf") ? "pdf" : "xlsx";
        String filename = "rankings-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmm")) + "." + ext;

        MediaType mediaType = type.equals("pdf")
                ? MediaType.APPLICATION_PDF
                : MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(mediaType)
                .body(bytes);
    }
}