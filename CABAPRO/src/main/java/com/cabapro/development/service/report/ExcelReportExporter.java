package com.cabapro.development.service.report;

import com.cabapro.development.model.Ranking;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class ExcelReportExporter implements ReportExporter {

    @Override
    public String getType() {
        return "xlsx";
    }

    @Override
    public byte[] exportRankings(List<Ranking> data) throws ReportException {
        try (Workbook wb = new XSSFWorkbook();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Sheet sheet = wb.createSheet("Rankings");
            int rowIdx = 0;

            // ===== Estilos =====
            Font bold = wb.createFont();
            bold.setBold(true);

            CellStyle headerStyle = wb.createCellStyle();
            headerStyle.setFont(bold);

            // ===== Encabezados =====
            Row header = sheet.createRow(rowIdx++);
            createCell(header, 0, "ID", headerStyle);
            createCell(header, 1, "Name", headerStyle);
            createCell(header, 2, "Fee", headerStyle);
            createCell(header, 3, "Description", headerStyle);

            // ===== Filas de datos =====
            for (Ranking r : data) {
                Row row = sheet.createRow(rowIdx++);

                // ID: si es null, dejamos celda vacía; si no, como número
                if (r.getId() != null) {
                    row.createCell(0).setCellValue(r.getId());
                } else {
                    row.createCell(0).setCellValue("");
                }

                // Name
                row.createCell(1).setCellValue(safe(r.getName()));

                // Fee (asumiendo getter double primitivo: no comparar con null ni llamar métodos)
                // Si quieres ocultar 0.0, déjalo vacío cuando sea 0.0
                double fee = r.getFee(); // <--- double primitivo
                if (Double.compare(fee, 0.0d) == 0) {
                    row.createCell(2).setCellValue("");
                } else if (Double.isNaN(fee) || Double.isInfinite(fee)) {
                    row.createCell(2).setCellValue("");
                } else {
                    row.createCell(2).setCellValue(fee);
                }

                // Description
                row.createCell(3).setCellValue(safe(r.getDescription()));
            }

            // Auto-ajustar ancho
            for (int i = 0; i < 4; i++) {
                sheet.autoSizeColumn(i);
            }

            wb.write(baos);
            return baos.toByteArray();

        } catch (Exception e) {
            throw new ReportException("Error generating XLSX report", e);
        }
    }

    private void createCell(Row row, int idx, String value, CellStyle style) {
        Cell cell = row.createCell(idx);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private String safe(String s) {
        return (s == null) ? "" : s;
    }
}