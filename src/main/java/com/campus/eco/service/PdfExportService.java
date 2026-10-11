package com.campus.eco.service;

import com.campus.eco.model.CarbonReport;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.util.List;

public class PdfExportService {

    public static void exportCarbonReport(String month, List<CarbonReport> reports, String path) throws Exception {
        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream(path));
        doc.open();

        Font title = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
        Font h = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font body = new Font(Font.FontFamily.HELVETICA, 10);

        Paragraph p = new Paragraph("Smart Campus Eco-Footprint System", title);
        p.setAlignment(Element.ALIGN_CENTER);
        doc.add(p);
        doc.add(new Paragraph("Carbon Report - " + month, h));
        doc.add(new Paragraph("Generated: " + java.time.LocalDateTime.now(), body));
        doc.add(Chunk.NEWLINE);

        PdfPTable t = new PdfPTable(6);
        t.setWidthPercentage(100);
        String[] cols = {"Dept ID", "Energy CO2", "Water CO2", "Transport CO2", "Waste CO2", "Total CO2 (kg)"};
        for (String c : cols) {
            PdfPCell cell = new PdfPCell(new Phrase(c, h));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            t.addCell(cell);
        }
        for (CarbonReport r : reports) {
            t.addCell(new Phrase(String.valueOf(r.getDeptId()), body));
            t.addCell(new Phrase(String.valueOf(r.getEnergyCo2()), body));
            t.addCell(new Phrase(String.valueOf(r.getWaterCo2()), body));
            t.addCell(new Phrase(String.valueOf(r.getTransportCo2()), body));
            t.addCell(new Phrase(String.valueOf(r.getWasteCo2()), body));
            t.addCell(new Phrase(String.valueOf(r.getTotalCo2()), body));
        }
        doc.add(t);
        doc.close();
    }
}
