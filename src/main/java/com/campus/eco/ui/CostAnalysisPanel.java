package com.campus.eco.ui;

import com.campus.eco.model.CostAnalysis;
import com.campus.eco.service.CostAnalysisService;
import com.campus.eco.util.UIHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CostAnalysisPanel extends JPanel implements Refreshable {
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"Dept","Month","Energy ₹","Water ₹","Transport ₹","Waste ₹","Total ₹","CO2 (kg)","₹/kg CO2"}, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
    private final JTable table = new JTable(model);
    private final JTextField monthF = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));
    private final CostAnalysisService svc = new CostAnalysisService();

    public CostAnalysisPanel() {
        setLayout(new BorderLayout(12, 12));
        setBackground(UIHelper.BG);
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        JLabel title = new JLabel("Cost vs Carbon Analyzer");
        title.setFont(UIHelper.H1); title.setForeground(UIHelper.PRIMARY_DARK);
        add(title, BorderLayout.NORTH);

        JPanel ctrl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ctrl.setBackground(UIHelper.BG);
        ctrl.add(new JLabel("Month:")); ctrl.add(monthF);
        JButton gen = new JButton("Generate Analysis"); UIHelper.styleButton(gen);
        gen.addActionListener(e -> generate());
        ctrl.add(gen);
        add(ctrl, BorderLayout.NORTH);

        table.setRowHeight(26); table.setFont(UIHelper.BODY); table.getTableHeader().setFont(UIHelper.BODY);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel tips = new JPanel(new GridLayout(3, 1)); tips.setBackground(UIHelper.CARD);
        tips.setBorder(BorderFactory.createTitledBorder("Reduction Suggestions"));
        tips.add(new JLabel(" • Switch to LED + solar to cut energy cost & carbon together."));
        tips.add(new JLabel(" • Rainwater harvesting reduces water cost and treatment emissions."));
        tips.add(new JLabel(" • Carpool/EV shuttles lower transport cost per kg CO2."));
        add(tips, BorderLayout.SOUTH);
    }

    private void generate() {
        try {
            List<CostAnalysis> list = svc.generateForMonth(monthF.getText().trim());
            model.setRowCount(0);
            for (CostAnalysis c : list) model.addRow(new Object[]{c.getDeptId(), c.getUsageMonth(), c.getEnergyCost(), c.getWaterCost(), c.getTransportCost(), c.getWasteCost(), c.getTotalCost(), c.getTotalCo2(), c.getCostPerCo2()});
            System.out.println("Cost analysis generated.");
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }

    @Override public void refresh() { generate(); }
}
