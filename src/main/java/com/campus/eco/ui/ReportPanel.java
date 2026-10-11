package com.campus.eco.ui;

import com.campus.eco.model.CarbonReport;
import com.campus.eco.service.CarbonCalculatorService;
import com.campus.eco.service.ExcelExportService;
import com.campus.eco.service.PdfExportService;
import com.campus.eco.util.UIHelper;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReportPanel extends JPanel implements Refreshable {
    private final JTextField monthF = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));
    private final CarbonCalculatorService svc = new CarbonCalculatorService();

    public ReportPanel() {
        setLayout(new BorderLayout(12, 12));
        setBackground(UIHelper.BG);
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        JLabel title = new JLabel("Report Generation & Export");
        title.setFont(UIHelper.H1); title.setForeground(UIHelper.PRIMARY_DARK);
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(2, 1, 16, 16));
        center.setBackground(UIHelper.BG);

        JPanel pdfCard = new JPanel(new BorderLayout(8, 8)); pdfCard.setBackground(UIHelper.CARD);
        pdfCard.setBorder(BorderFactory.createTitledBorder("PDF Export"));
        JLabel pl = new JLabel("Generate a professional PDF carbon report for the selected month.");
        pl.setFont(UIHelper.BODY); pl.setForeground(UIHelper.MUTED);
        pdfCard.add(pl, BorderLayout.CENTER);
        JButton pdfBtn = new JButton("Export PDF"); UIHelper.styleButton(pdfBtn);
        pdfBtn.addActionListener(e -> exportPdf());
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT)); p1.setBackground(UIHelper.CARD);
        p1.add(new JLabel("Month:")); p1.add(monthF); p1.add(pdfBtn);
        pdfCard.add(p1, BorderLayout.SOUTH);
        center.add(pdfCard);

        JPanel xlsCard = new JPanel(new BorderLayout(8, 8)); xlsCard.setBackground(UIHelper.CARD);
        xlsCard.setBorder(BorderFactory.createTitledBorder("Excel Export"));
        JLabel xl = new JLabel("Generate an Excel (.xlsx) carbon report for spreadsheet analysis.");
        xl.setFont(UIHelper.BODY); xl.setForeground(UIHelper.MUTED);
        xlsCard.add(xl, BorderLayout.CENTER);
        JButton xlsBtn = new JButton("Export Excel"); UIHelper.styleButton(xlsBtn);
        xlsBtn.addActionListener(e -> exportExcel());
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT)); p2.setBackground(UIHelper.CARD);
        p2.add(new JLabel("Month:")); JTextField m2 = new JTextField(monthF.getText(), 8); p2.add(m2);
        p2.add(xlsBtn);
        xlsCard.add(p2, BorderLayout.SOUTH);
        center.add(xlsCard);

        add(center, BorderLayout.CENTER);
    }

    private void exportPdf() {
        String m = monthF.getText().trim();
        JFileChooser fc = new JFileChooser(new File("carbon_report_" + m + ".pdf"));
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        try {
            List<CarbonReport> list = svc.generateForMonth(m);
            PdfExportService.exportCarbonReport(m, list, fc.getSelectedFile().getAbsolutePath());
            System.out.println("PDF saved.");
        } catch (Exception ex) { UIHelper.err("Export failed: " + ex.getMessage()); }
    }

    private void exportExcel() {
        String m = monthF.getText().trim();
        JFileChooser fc = new JFileChooser(new File("carbon_report_" + m + ".xlsx"));
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        try {
            List<CarbonReport> list = svc.generateForMonth(m);
            ExcelExportService.exportCarbonReport(m, list, fc.getSelectedFile().getAbsolutePath());
            System.out.println("Excel saved.");
        } catch (Exception ex) { UIHelper.err("Export failed: " + ex.getMessage()); }
    }

    @Override public void refresh() {}
}
