package com.campus.eco.ui;

import com.campus.eco.dao.DepartmentDAO;
import com.campus.eco.dao.TreeDAO;
import com.campus.eco.model.Department;
import com.campus.eco.model.TreeTracker;
import com.campus.eco.util.UIHelper;
import com.campus.eco.util.Validation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TreePanel extends JPanel implements Refreshable {
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"ID","Dept","Count","Species","Planted","Absorption (kg)","Location"}, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
    private final JTable table = new JTable(model);
    private final JComboBox<Department> deptBox = new JComboBox<>();
    private final JTextField countF = new JTextField(), speciesF = new JTextField(), dateF = new JTextField(), locF = new JTextField();
    private final TreeDAO dao = new TreeDAO();

    public TreePanel() {
        setLayout(new BorderLayout(12, 12));
        setBackground(UIHelper.BG);
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        JLabel title = new JLabel("Campus Tree Impact Tracker");
        title.setFont(UIHelper.H1); title.setForeground(UIHelper.PRIMARY_DARK);
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.setBackground(UIHelper.CARD); form.setBorder(BorderFactory.createTitledBorder("Tree Plantation"));
        form.add(new JLabel("Department:")); form.add(deptBox);
        form.add(new JLabel("Tree Count:")); form.add(countF);
        form.add(new JLabel("Species:")); form.add(speciesF);
        form.add(new JLabel("Planted Date (YYYY-MM-DD):")); dateF.setText(LocalDate.now().toString()); form.add(dateF);
        form.add(new JLabel("Location:")); form.add(locF);

        JButton add = new JButton("Plant"); UIHelper.styleButton(add);
        add.addActionListener(e -> plant());
        JButton del = new JButton("Delete"); UIHelper.styleDangerButton(del);
        del.addActionListener(e -> { int r = table.getSelectedRow(); if (r>=0) { try { dao.delete((int)model.getValueAt(r,0)); refresh(); } catch(Exception ex){UIHelper.err(ex.getMessage());} } });
        JPanel bp = new JPanel(new FlowLayout(FlowLayout.LEFT)); bp.setBackground(UIHelper.BG); bp.add(add); bp.add(del);
        JPanel left = new JPanel(new BorderLayout()); left.setBackground(UIHelper.BG); left.setPreferredSize(new Dimension(340,0));
        left.add(form, BorderLayout.CENTER); left.add(bp, BorderLayout.SOUTH);
        add(left, BorderLayout.WEST);

        table.setRowHeight(26); table.setFont(UIHelper.BODY); table.getTableHeader().setFont(UIHelper.BODY);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void plant() {
        if (!Validation.isInt(countF.getText())) { UIHelper.err("Enter valid count."); return; }
        try {
            TreeTracker t = new TreeTracker();
            if (deptBox.getSelectedItem() != null) t.setDeptId(((Department) deptBox.getSelectedItem()).getDeptId());
            t.setTreeCount(Integer.parseInt(countF.getText().trim()));
            t.setSpecies(speciesF.getText().trim());
            t.setPlantedDate(LocalDate.parse(dateF.getText().trim()));
            t.setLocation(locF.getText().trim());
            if (dao.insert(t)) { System.out.println("Tree record added."); refresh(); }
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }

    @Override public void refresh() {
        try {
            deptBox.removeAllItems();
            for (Department d : new DepartmentDAO().findAll()) deptBox.addItem(d);
            model.setRowCount(0);
            for (TreeTracker t : dao.findAll()) model.addRow(new Object[]{t.getTreeId(), t.getDeptId(), t.getTreeCount(), t.getSpecies(), t.getPlantedDate(), t.getCarbonAbsorptionKg(), t.getLocation()});
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }
}
