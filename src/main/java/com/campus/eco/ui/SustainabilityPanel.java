package com.campus.eco.ui;

import com.campus.eco.dao.DepartmentDAO;
import com.campus.eco.model.Department;
import com.campus.eco.model.SustainabilityScore;
import com.campus.eco.service.SustainabilityScoreService;
import com.campus.eco.util.UIHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SustainabilityPanel extends JPanel implements Refreshable {
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"Dept","Month","Energy","Water","Waste","Transport","Overall","Rating"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);
    private final JTextField monthF = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));
    private final SustainabilityScoreService svc = new SustainabilityScoreService();

    public SustainabilityPanel() {
        setLayout(new BorderLayout(12, 12));
        setBackground(UIHelper.BG);
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        JLabel title = new JLabel("Sustainability Scorecard");
        title.setFont(UIHelper.H1); title.setForeground(UIHelper.PRIMARY_DARK);
        add(title, BorderLayout.NORTH);

        JPanel ctrl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ctrl.setBackground(UIHelper.BG);
        ctrl.add(new JLabel("Month:")); ctrl.add(monthF);
        JButton calc = new JButton("Compute Scores"); UIHelper.styleButton(calc);
        calc.addActionListener(e -> compute());
        ctrl.add(calc);
        add(ctrl, BorderLayout.NORTH);

        table.setRowHeight(26); table.setFont(UIHelper.BODY); table.getTableHeader().setFont(UIHelper.BODY);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void compute() {
        String m = monthF.getText().trim();
        try {
            List<SustainabilityScore> list = svc.computeAndPersist(m);
            Map<Integer, String> deptNames = new HashMap<>();
            for (Department d : new DepartmentDAO().findAll()) deptNames.put(d.getDeptId(), d.getDeptCode());
            model.setRowCount(0);
            for (SustainabilityScore s : list) model.addRow(new Object[]{deptNames.getOrDefault(s.getDeptId(), String.valueOf(s.getDeptId())), s.getUsageMonth(), s.getEnergyScore(), s.getWaterScore(), s.getWasteScore(), s.getTransportScore(), s.getOverallScore(), s.getRating()});
            System.out.println("Scores computed for " + m);
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }

    @Override public void refresh() { compute(); }
}
