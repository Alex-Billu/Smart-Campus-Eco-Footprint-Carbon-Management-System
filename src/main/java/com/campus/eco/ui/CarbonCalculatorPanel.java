package com.campus.eco.ui;

import com.campus.eco.model.CarbonReport;
import com.campus.eco.service.CarbonCalculatorService;
import com.campus.eco.util.UIHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CarbonCalculatorPanel extends JPanel implements Refreshable {
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"Dept ID","Energy CO2","Water CO2","Transport CO2","Waste CO2","Total CO2 (kg)"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);
    private final JTextField monthF = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));
    private final CarbonCalculatorService svc = new CarbonCalculatorService();
    private final JLabel totalL = new JLabel("--"), netL = new JLabel("--"), treesL = new JLabel("--");

    public CarbonCalculatorPanel() {
        setLayout(new BorderLayout(12, 12));
        setBackground(UIHelper.BG);
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        JLabel title = new JLabel("Carbon Footprint Calculator");
        title.setFont(UIHelper.H1); title.setForeground(UIHelper.PRIMARY_DARK);
        add(title, BorderLayout.NORTH);

        JPanel ctrl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ctrl.setBackground(UIHelper.BG);
        ctrl.add(new JLabel("Month:")); ctrl.add(monthF);
        JButton gen = new JButton("Generate Report"); UIHelper.styleButton(gen);
        gen.addActionListener(e -> generate());
        ctrl.add(gen);
        add(ctrl, BorderLayout.NORTH);

        table.setRowHeight(26); table.setFont(UIHelper.BODY); table.getTableHeader().setFont(UIHelper.BODY);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel summary = new JPanel(new GridLayout(1, 3, 12, 12));
        summary.setBackground(UIHelper.BG);
        summary.add(summaryCard("Total Emission (kg)", totalL, new Color(214, 64, 64)));
        summary.add(summaryCard("Tree Absorption (kg)", treesL, UIHelper.PRIMARY));
        summary.add(summaryCard("Net Footprint (kg)", netL, new Color(120, 80, 160)));
        summary.setPreferredSize(new Dimension(0, 90));
        add(summary, BorderLayout.SOUTH);
    }

    private JPanel summaryCard(String label, JLabel val, Color accent) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(UIHelper.CARD);
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(225,230,226),1), BorderFactory.createEmptyBorder(12,16,12,16)));
        JPanel bar = new JPanel(); bar.setBackground(accent); bar.setPreferredSize(new Dimension(4,0));
        p.add(bar, BorderLayout.WEST);
        JLabel l = new JLabel(label); l.setFont(UIHelper.SMALL); l.setForeground(UIHelper.MUTED);
        val.setFont(new Font("Segoe UI", Font.BOLD, 22)); val.setForeground(UIHelper.TEXT);
        p.add(l, BorderLayout.NORTH); p.add(val, BorderLayout.CENTER);
        return p;
    }

    private void generate() {
        String m = monthF.getText().trim();
        try {
            List<CarbonReport> list = svc.generateForMonth(m);
            model.setRowCount(0);
            for (CarbonReport r : list) model.addRow(new Object[]{r.getDeptId(), r.getEnergyCo2(), r.getWaterCo2(), r.getTransportCo2(), r.getWasteCo2(), r.getTotalCo2()});
            totalL.setText(String.valueOf(svc.campusTotal(m)));
            treesL.setText(String.valueOf(svc.totalTreeAbsorption()));
            netL.setText(String.valueOf(svc.netCampusFootprint(m)));
            System.out.println("Carbon report generated for " + m);
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }

    @Override public void refresh() { generate(); }
}
