package com.campus.eco.service;

import com.campus.eco.model.CarbonReport;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.List;

public class ExcelExportService {

    public static void exportCarbonReport(String month, List<CarbonReport> reports, String path) throws Exception {
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Carbon Report " + month);
            CellStyle header = wb.createCellStyle();
            Font f = wb.createFont(); f.setBold(true); header.setFont(f);
            header.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
            header.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            String[] cols = {"Dept ID", "Energy CO2", "Water CO2", "Transport CO2", "Waste CO2", "Total CO2 (kg)"};
            Row hr = sheet.createRow(0);
            for (int i = 0; i < cols.length; i++) {
                Cell c = hr.createCell(i); c.setCellValue(cols[i]); c.setCellStyle(header);
            }
            int r = 1;
            for (CarbonReport rep : reports) {
                Row row = sheet.createRow(r++);
                row.createCell(0).setCellValue(rep.getDeptId() == null ? 0 : rep.getDeptId());
                row.createCell(1).setCellValue(rep.getEnergyCo2() == null ? 0 : rep.getEnergyCo2().doubleValue());
                row.createCell(2).setCellValue(rep.getWaterCo2() == null ? 0 : rep.getWaterCo2().doubleValue());
                row.createCell(3).setCellValue(rep.getTransportCo2() == null ? 0 : rep.getTransportCo2().doubleValue());
                row.createCell(4).setCellValue(rep.getWasteCo2() == null ? 0 : rep.getWasteCo2().doubleValue());
                row.createCell(5).setCellValue(rep.getTotalCo2() == null ? 0 : rep.getTotalCo2().doubleValue());
            }
            for (int i = 0; i < cols.length; i++) sheet.autoSizeColumn(i);
            try (FileOutputStream out = new FileOutputStream(path)) { wb.write(out); }
        }
    }
}
