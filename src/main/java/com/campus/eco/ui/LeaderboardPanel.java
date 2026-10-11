package com.campus.eco.ui;

import com.campus.eco.dao.DepartmentDAO;
import com.campus.eco.dao.GreenPointsDAO;
import com.campus.eco.model.Department;
import com.campus.eco.service.CarbonCalculatorService;
import com.campus.eco.service.ChartService;
import com.campus.eco.util.UIHelper;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaderboardPanel extends JPanel implements Refreshable {
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"Rank","Department","Green Points","Carbon (kg)","Score"}, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
    private final JTable table = new JTable(model);
    private final JTextField monthF = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));
    private final JPanel chartHolder = new JPanel(new GridLayout(1, 1, 12, 12));

    public LeaderboardPanel() {
        setLayout(new BorderLayout(12, 12));
        setBackground(UIHelper.BG);
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        JLabel title = new JLabel("Green Campus Leaderboard");
        title.setFont(UIHelper.H1); title.setForeground(UIHelper.PRIMARY_DARK);
        add(title, BorderLayout.NORTH);

        JPanel ctrl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ctrl.setBackground(UIHelper.BG);
        ctrl.add(new JLabel("Month:")); ctrl.add(monthF);
        JButton load = new JButton("Load"); UIHelper.styleButton(load);
        load.addActionListener(e -> refresh());
        ctrl.add(load);
        add(ctrl, BorderLayout.NORTH);

        table.setRowHeight(28); table.setFont(UIHelper.BODY); table.getTableHeader().setFont(UIHelper.BODY);
        add(new JScrollPane(table), BorderLayout.CENTER);
        chartHolder.setBackground(UIHelper.BG);
        chartHolder.setPreferredSize(new Dimension(0, 240));
        add(chartHolder, BorderLayout.SOUTH);
    }

    @Override public void refresh() {
        String m = monthF.getText().trim();
        try {
            Map<Integer, String> names = new HashMap<>();
            Map<Integer, Integer> points = new HashMap<>();
            for (Department d : new DepartmentDAO().findAll()) { names.put(d.getDeptId(), d.getDeptCode()); points.put(d.getDeptId(), 0); }
            for (int[] p : new GreenPointsDAO().leaderboard()) points.put(p[0], p[1]);

            Map<String, BigDecimal> carbon = new HashMap<>();
            try (Connection c = com.campus.eco.config.DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement("SELECT dept_id, total_co2 FROM Carbon_Report WHERE usage_month=?")) {
                ps.setString(1, m);
                try (ResultSet rs = ps.executeQuery()) { while (rs.next()) carbon.put(names.getOrDefault(rs.getInt("dept_id"), String.valueOf(rs.getInt("dept_id"))), rs.getBigDecimal("total_co2")); }
            }
            new CarbonCalculatorService().generateForMonth(m);

            model.setRowCount(0);
            int rank = 1;
            for (Department d : new DepartmentDAO().findAll()) {
                int pts = points.getOrDefault(d.getDeptId(), 0);
                BigDecimal carb = carbon.getOrDefault(d.getDeptCode(), BigDecimal.ZERO);
                model.addRow(new Object[]{rank++, d.getDeptCode(), pts, carb, "--"});
            }

            // chart
            Map<String, Integer> chartData = new HashMap<>();
            points.forEach((id, p) -> chartData.put(names.getOrDefault(id, String.valueOf(id)), p));
            JFreeChart chart = ChartService.barChart("Green Points Leaderboard", "Department", "Points", chartData);
            chartHolder.removeAll();
            JPanel p = new JPanel(new BorderLayout()); p.setBackground(UIHelper.CARD);
            p.setBorder(BorderFactory.createLineBorder(new Color(225,230,226),1));
            p.add(new JLabel(new ImageIcon(chart.createBufferedImage(700, 220))), BorderLayout.CENTER);
            chartHolder.add(p);
            chartHolder.revalidate(); chartHolder.repaint();
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }
}
