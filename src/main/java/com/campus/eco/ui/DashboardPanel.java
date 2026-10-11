package com.campus.eco.ui;

import com.campus.eco.config.DBConnection;
import com.campus.eco.dao.DepartmentDAO;
import com.campus.eco.dao.TreeDAO;
import com.campus.eco.model.Department;
import com.campus.eco.service.CarbonCalculatorService;
import com.campus.eco.service.ChartService;
import com.campus.eco.util.UIHelper;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class DashboardPanel extends JPanel implements Refreshable {
    private final JLabel energyVal = new JLabel("--", SwingConstants.CENTER);
    private final JLabel waterVal = new JLabel("--", SwingConstants.CENTER);
    private final JLabel wasteVal = new JLabel("--", SwingConstants.CENTER);
    private final JLabel carbonVal = new JLabel("--", SwingConstants.CENTER);
    private final JLabel netVal = new JLabel("--", SwingConstants.CENTER);
    private final JLabel treesVal = new JLabel("--", SwingConstants.CENTER);
    private final JLabel scoreVal = new JLabel("--", SwingConstants.CENTER);
    private final JLabel deptsVal = new JLabel("--", SwingConstants.CENTER);
    private final JPanel chartHolder = new JPanel(new GridLayout(1, 2, 12, 12));
    private final JComboBox<String> monthBox = new JComboBox<>();

    public DashboardPanel() {
        setLayout(new BorderLayout(12, 12));
        setBackground(UIHelper.BG);
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(UIHelper.BG);
        JLabel title = new JLabel("Campus Sustainability Dashboard");
        title.setFont(UIHelper.H1); title.setForeground(UIHelper.PRIMARY_DARK);
        top.add(title, BorderLayout.WEST);
        JPanel mp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        mp.setBackground(UIHelper.BG);
        mp.add(new JLabel("Month:"));
        for (int i = 0; i < 12; i++) {
            String m = LocalDate.now().minusMonths(i).format(DateTimeFormatter.ofPattern("yyyy-MM"));
            monthBox.addItem(m);
        }
        mp.add(monthBox);
        JButton refresh = new JButton("Refresh");
        UIHelper.styleButton(refresh);
        refresh.addActionListener(e -> refresh());
        mp.add(refresh);
        top.add(mp, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(2, 4, 12, 12));
        cards.setBackground(UIHelper.BG);
        cards.add(kpiCard("Energy (kWh)", energyVal, new Color(52, 118, 196)));
        cards.add(kpiCard("Water (L)", waterVal, new Color(40, 160, 200)));
        cards.add(kpiCard("Waste (kg)", wasteVal, new Color(232, 160, 40)));
        cards.add(kpiCard("Carbon (kg CO2)", carbonVal, new Color(214, 64, 64)));
        cards.add(kpiCard("Net Footprint", netVal, new Color(120, 80, 160)));
        cards.add(kpiCard("Trees Planted", treesVal, UIHelper.PRIMARY));
        cards.add(kpiCard("Sustainability", scoreVal, UIHelper.SUCCESS));
        cards.add(kpiCard("Departments", deptsVal, UIHelper.PRIMARY_DARK));
        add(cards, BorderLayout.CENTER);

        chartHolder.setBackground(UIHelper.BG);
        add(chartHolder, BorderLayout.SOUTH);
    }

    private JPanel kpiCard(String label, JLabel value, Color accent) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(UIHelper.CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 230, 226), 1),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)));
        JPanel accentBar = new JPanel();
        accentBar.setBackground(accent);
        accentBar.setPreferredSize(new Dimension(4, 0));
        p.add(accentBar, BorderLayout.WEST);
        JLabel l = new JLabel(label); l.setFont(UIHelper.SMALL); l.setForeground(UIHelper.MUTED);
        value.setFont(new Font("Segoe UI", Font.BOLD, 24)); value.setForeground(UIHelper.TEXT);
        p.add(l, BorderLayout.NORTH);
        p.add(value, BorderLayout.CENTER);
        return p;
    }

    @Override
    public void refresh() {
        String month = (String) monthBox.getSelectedItem();
        try {
            BigDecimal energy = sum("SELECT COALESCE(SUM(electricity_kwh),0) FROM Energy_Usage WHERE usage_month=?", month);
            BigDecimal water = sum("SELECT COALESCE(SUM(water_litres),0) FROM Water_Usage WHERE usage_month=?", month);
            BigDecimal waste = sum("SELECT COALESCE(SUM(recyclable_kg)+SUM(non_recyclable_kg),0) FROM Waste_Record WHERE usage_month=?", month);
            CarbonCalculatorService cs = new CarbonCalculatorService();
            cs.generateForMonth(month);
            BigDecimal carbon = cs.campusTotal(month);
            BigDecimal net = cs.netCampusFootprint(month);
            int trees = new TreeDAO().totalTrees();
            int depts = new DepartmentDAO().findAll().size();
            BigDecimal score = avgScore(month);

            energyVal.setText(energy.toPlainString());
            waterVal.setText(water.toPlainString());
            wasteVal.setText(waste.toPlainString());
            carbonVal.setText(carbon.toPlainString());
            netVal.setText(net.toPlainString());
            treesVal.setText(String.valueOf(trees));
            deptsVal.setText(String.valueOf(depts));
            scoreVal.setText(score == null ? "--" : score.toPlainString());

            renderCharts(month);
        } catch (Exception e) {
            UIHelper.err("Dashboard load error: " + e.getMessage());
        }
    }

    private void renderCharts(String month) throws SQLException {
        chartHolder.removeAll();
        // Pie: carbon by source
        Map<String, BigDecimal> pie = new HashMap<>();
        try (Connection c = DBConnection.getConnection()) {
            pie.put("Energy", sum(c, "SELECT COALESCE(SUM(co2_emission_kg),0) FROM Energy_Usage WHERE usage_month=?", month));
            pie.put("Water", sum(c, "SELECT COALESCE(SUM(co2_emission_kg),0) FROM Water_Usage WHERE usage_month=?", month));
            pie.put("Transport", sum(c, "SELECT COALESCE(SUM(co2_emission_kg),0) FROM Transport_Details WHERE usage_month=?", month));
            pie.put("Waste", sum(c, "SELECT COALESCE(SUM(co2_emission_kg),0) FROM Waste_Record WHERE usage_month=?", month));
        }
        JFreeChart pieChart = ChartService.pieChart("Carbon by Source - " + month, pie);
        chartHolder.add(wrapChart(pieChart, 380, 240));

        // Bar: department carbon
        Map<String, BigDecimal> bar = new HashMap<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT d.dept_code, COALESCE(r.total_co2,0) t FROM Department d " +
                             "LEFT JOIN Carbon_Report r ON d.dept_id=r.dept_id AND r.usage_month=? ORDER BY t DESC")) {
            ps.setString(1, month);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) bar.put(rs.getString("dept_code"), rs.getBigDecimal("t"));
            }
        }
        JFreeChart barChart = ChartService.barChart("Department Carbon - " + month, "Department", "CO2 (kg)", bar);
        chartHolder.add(wrapChart(barChart, 380, 240));
        chartHolder.revalidate();
        chartHolder.repaint();
    }

    private JPanel wrapChart(JFreeChart chart, int w, int h) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(UIHelper.CARD);
        p.setBorder(BorderFactory.createLineBorder(new Color(225, 230, 226), 1));
        JLabel lbl = new JLabel(new ImageIcon(chart.createBufferedImage(w, h)));
        p.add(lbl, BorderLayout.CENTER);
        return p;
    }

    private BigDecimal sum(String sql, String month) throws SQLException {
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, month);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO; }
        }
    }
    private BigDecimal sum(Connection c, String sql, String month) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, month);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO; }
        }
    }
    private BigDecimal avgScore(String month) throws SQLException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT COALESCE(AVG(overall_score),0) FROM Sustainability_Score WHERE usage_month=?")) {
            ps.setString(1, month);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO; }
        }
    }
}
