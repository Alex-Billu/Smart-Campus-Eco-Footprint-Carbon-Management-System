package com.campus.eco.ui;

import com.campus.eco.dao.DepartmentDAO;
import com.campus.eco.dao.GreenPointsDAO;
import com.campus.eco.model.Department;
import com.campus.eco.model.GreenPoints;
import com.campus.eco.util.UIHelper;
import com.campus.eco.util.Validation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GreenPointsPanel extends JPanel implements Refreshable {
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"ID","Dept","Month","Points","Reason"}, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
    private final JTable table = new JTable(model);
    private final JComboBox<Department> deptBox = new JComboBox<>();
    private final JTextField monthF = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));
    private final JTextField pointsF = new JTextField();
    private final JTextField reasonF = new JTextField();
    private final GreenPointsDAO dao = new GreenPointsDAO();

    public GreenPointsPanel() {
        setLayout(new BorderLayout(12, 12));
        setBackground(UIHelper.BG);
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        JLabel title = new JLabel("Green Points Reward System");
        title.setFont(UIHelper.H1); title.setForeground(UIHelper.PRIMARY_DARK);
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.setBackground(UIHelper.CARD); form.setBorder(BorderFactory.createTitledBorder("Award Points"));
        form.add(new JLabel("Department:")); form.add(deptBox);
        form.add(new JLabel("Month:")); form.add(monthF);
        form.add(new JLabel("Points:")); form.add(pointsF);
        form.add(new JLabel("Reason:")); form.add(reasonF);

        JButton add = new JButton("Award"); UIHelper.styleButton(add);
        add.addActionListener(e -> award());
        JPanel bp = new JPanel(new FlowLayout(FlowLayout.LEFT)); bp.setBackground(UIHelper.BG); bp.add(add);
        JPanel left = new JPanel(new BorderLayout()); left.setBackground(UIHelper.BG); left.setPreferredSize(new Dimension(340,0));
        left.add(form, BorderLayout.CENTER); left.add(bp, BorderLayout.SOUTH);
        add(left, BorderLayout.WEST);

        table.setRowHeight(26); table.setFont(UIHelper.BODY); table.getTableHeader().setFont(UIHelper.BODY);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void award() {
        if (deptBox.getSelectedItem() == null || !Validation.isInt(pointsF.getText())) { UIHelper.err("Select dept & enter points."); return; }
        try {
            GreenPoints g = new GreenPoints();
            g.setDeptId(((Department) deptBox.getSelectedItem()).getDeptId());
            g.setUsageMonth(monthF.getText().trim());
            g.setPoints(Integer.parseInt(pointsF.getText().trim()));
            g.setReason(reasonF.getText().trim());
            if (dao.insert(g)) { System.out.println("Points awarded."); refresh(); }
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }

    @Override public void refresh() {
        try {
            deptBox.removeAllItems();
            for (Department d : new DepartmentDAO().findAll()) deptBox.addItem(d);
            model.setRowCount(0);
            for (GreenPoints g : dao.findAll()) model.addRow(new Object[]{g.getPointId(), g.getDeptId(), g.getUsageMonth(), g.getPoints(), g.getReason()});
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }
}
