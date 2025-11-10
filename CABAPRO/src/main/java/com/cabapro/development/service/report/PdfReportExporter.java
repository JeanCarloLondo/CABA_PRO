package com.cabapro.development.service.report;

import com.cabapro.development.model.Ranking;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class PdfReportExporter implements ReportExporter {

    @Override
    public String getType() {
        return "pdf";
    }

    @Override
    public byte[] exportRankings(List<Ranking> data) throws ReportException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document doc = new Document(PageSize.A4.rotate(), 36, 36, 36, 36);
            PdfWriter.getInstance(doc, baos);

            doc.open();

            // Título
            Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
            Paragraph title = new Paragraph("Rankings Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            doc.add(title);
            doc.add(new Paragraph(" ")); // Espaciador

            // Tabla
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100f);
            table.setWidths(new float[]{8f, 28f, 10f, 54f});

            addHeader(table, "ID");
            addHeader(table, "Name");
            addHeader(table, "Fee");
            addHeader(table, "Description");

            for (Ranking r : data) {
                // ID
                table.addCell(safe(r.getId() == null ? "" : String.valueOf(r.getId())));

                // Name
                table.addCell(safe(r.getName()));

                // Fee (asumiendo getter double primitivo)
                double fee = r.getFee(); // <--- double primitivo
                String feeTxt;
                if (Double.compare(fee, 0.0d) == 0 || Double.isNaN(fee) || Double.isInfinite(fee)) {
                    feeTxt = "";
                } else {
                    feeTxt = String.valueOf(fee);
                }
                table.addCell(feeTxt);

                // Description
                table.addCell(safe(r.getDescription()));
            }

            doc.add(table);
            doc.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new ReportException("Error generating PDF report", e);
        }
    }

    private void addHeader(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, new Font(Font.HELVETICA, 12, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private String safe(String s) {
        return (s == null) ? "" : s;
    }
}