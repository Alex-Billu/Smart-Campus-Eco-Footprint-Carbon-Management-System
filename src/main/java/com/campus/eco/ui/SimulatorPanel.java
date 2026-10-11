package com.campus.eco.ui;

import com.campus.eco.service.SimulatorService;
import com.campus.eco.util.UIHelper;
import com.campus.eco.util.Validation;

import javax.swing.*;
import java.awt.*;

public class SimulatorPanel extends JPanel implements Refreshable {
    private final JTextField ledF = new JTextField(), treesF = new JTextField(), kmF = new JTextField();
    private final JLabel carbonL = new JLabel("--", SwingConstants.CENTER), moneyL = new JLabel("--", SwingConstants.CENTER), energyL = new JLabel("--", SwingConstants.CENTER);

    public SimulatorPanel() {
        setLayout(new BorderLayout(12, 12));
        setBackground(UIHelper.BG);
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        JLabel title = new JLabel("Carbon Savings Simulator");
        title.setFont(UIHelper.H1); title.setForeground(UIHelper.PRIMARY_DARK);
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        form.setBackground(UIHelper.CARD); form.setBorder(BorderFactory.createTitledBorder("Scenario Inputs"));
        form.add(new JLabel("LED Installations:")); form.add(ledF);
        form.add(new JLabel("Trees to Plant:")); form.add(treesF);
        form.add(new JLabel("Reduced Vehicle Usage (km/month):")); form.add(kmF);

        JButton sim = new JButton("Simulate Savings"); UIHelper.styleButton(sim);
        sim.addActionListener(e -> simulate());
        JPanel bp = new JPanel(new FlowLayout(FlowLayout.LEFT)); bp.setBackground(UIHelper.BG); bp.add(sim);

        JPanel left = new JPanel(new BorderLayout(8, 8)); left.setBackground(UIHelper.BG); left.setPreferredSize(new Dimension(380, 0));
        left.add(form, BorderLayout.CENTER); left.add(bp, BorderLayout.SOUTH);
        add(left, BorderLayout.WEST);

        JPanel out = new JPanel(new GridLayout(3, 1, 12, 12));
        out.setBackground(UIHelper.BG);
        out.add(resultCard("Carbon Saved (kg CO2)", carbonL, new Color(46, 160, 90)));
        out.add(resultCard("Money Saved (₹)", moneyL, new Color(52, 118, 196)));
        out.add(resultCard("Energy Saved (kWh)", energyL, new Color(232, 160, 40)));
        add(out, BorderLayout.CENTER);
    }

    private JPanel resultCard(String label, JLabel val, Color accent) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(UIHelper.CARD);
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(225,230,226),1), BorderFactory.createEmptyBorder(16,18,16,18)));
        JPanel bar = new JPanel(); bar.setBackground(accent); bar.setPreferredSize(new Dimension(4,0));
        p.add(bar, BorderLayout.WEST);
        JLabel l = new JLabel(label); l.setFont(UIHelper.BODY); l.setForeground(UIHelper.MUTED);
        val.setFont(new Font("Segoe UI", Font.BOLD, 26)); val.setForeground(UIHelper.TEXT);
        p.add(l, BorderLayout.NORTH); p.add(val, BorderLayout.CENTER);
        return p;
    }

    private void simulate() {
        int led = Validation.isInt(ledF.getText()) ? Integer.parseInt(ledF.getText().trim()) : 0;
        int trees = Validation.isInt(treesF.getText()) ? Integer.parseInt(treesF.getText().trim()) : 0;
        int km = Validation.isInt(kmF.getText()) ? Integer.parseInt(kmF.getText().trim()) : 0;
        SimulatorService.SimResult r = new SimulatorService().simulate(led, trees, km);
        carbonL.setText(String.valueOf(r.totalCarbonSaved));
        moneyL.setText(String.valueOf(r.totalMoneySaved));
        energyL.setText(String.valueOf(r.energySavedKwh));
    }

    @Override public void refresh() {}
}
