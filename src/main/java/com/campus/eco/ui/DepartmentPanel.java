package com.campus.eco.ui;

import com.campus.eco.dao.DepartmentDAO;
import com.campus.eco.model.Department;
import com.campus.eco.util.UIHelper;
import com.campus.eco.util.Validation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class DepartmentPanel extends JPanel implements Refreshable {
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"ID","Name","Code","Building","Area (sqm)","Staff"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);
    private final JTextField nameF = new JTextField(), codeF = new JTextField(), bldgF = new JTextField();
    private final JTextField areaF = new JTextField(), staffF = new JTextField();
    private final JTextField searchF = new JTextField();
    private final DepartmentDAO dao = new DepartmentDAO();

    public DepartmentPanel() {
        setLayout(new BorderLayout(12, 12));
        setBackground(UIHelper.BG);
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel title = new JLabel("Department Management");
        title.setFont(UIHelper.H1); title.setForeground(UIHelper.PRIMARY_DARK);
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.setBackground(UIHelper.CARD);
        form.setBorder(BorderFactory.createTitledBorder("Department Details"));
        form.add(new JLabel("Name:")); form.add(nameF);
        form.add(new JLabel("Code:")); form.add(codeF);
        form.add(new JLabel("Building:")); form.add(bldgF);
        form.add(new JLabel("Floor Area (sqm):")); form.add(areaF);
        form.add(new JLabel("Staff Count:")); form.add(staffF);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton add = new JButton("Add"), upd = new JButton("Update"), del = new JButton("Delete"), clr = new JButton("Clear");
        UIHelper.styleButton(add); UIHelper.styleSecondaryButton(upd); UIHelper.styleDangerButton(del); UIHelper.styleSecondaryButton(clr);
        btns.add(add); btns.add(upd); btns.add(del); btns.add(clr);

        JPanel left = new JPanel(new BorderLayout(8, 8));
        left.setBackground(UIHelper.BG);
        left.add(form, BorderLayout.CENTER);
        left.add(btns, BorderLayout.SOUTH);
        left.setPreferredSize(new Dimension(360, 0));

        JPanel right = new JPanel(new BorderLayout(8, 8));
        right.setBackground(UIHelper.BG);
        JPanel sp = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sp.setBackground(UIHelper.BG);
        sp.add(new JLabel("Search:")); sp.add(searchF);
        searchF.setPreferredSize(new Dimension(180, 28));
        JButton search = new JButton("Search"); UIHelper.styleSecondaryButton(search);
        JButton all = new JButton("Show All"); UIHelper.styleSecondaryButton(all);
        sp.add(search); sp.add(all);
        right.add(sp, BorderLayout.NORTH);
        table.setRowHeight(26); table.getTableHeader().setFont(UIHelper.BODY); table.setFont(UIHelper.BODY);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        right.add(new JScrollPane(table), BorderLayout.CENTER);

        add(left, BorderLayout.WEST);
        add(right, BorderLayout.CENTER);

        add.addActionListener(e -> add());
        upd.addActionListener(e -> update());
        del.addActionListener(e -> delete());
        clr.addActionListener(e -> clear());
        search.addActionListener(e -> search());
        all.addActionListener(e -> refresh());
        table.getSelectionModel().addListSelectionListener(e -> { if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) loadRow(); });
    }

    private void add() {
        if (!Validation.notBlank(nameF.getText()) || !Validation.notBlank(codeF.getText())) { UIHelper.err("Name and Code required."); return; }
        try {
            Department d = new Department();
            d.setDeptName(nameF.getText().trim());
            d.setDeptCode(codeF.getText().trim().toUpperCase());
            d.setBuilding(bldgF.getText().trim());
            d.setFloorAreaSqm(Validation.isDecimal(areaF.getText()) ? new BigDecimal(areaF.getText().trim()) : BigDecimal.ZERO);
            d.setStaffCount(Validation.isInt(staffF.getText()) ? Integer.parseInt(staffF.getText().trim()) : 0);
            if (dao.insert(d)) { System.out.println("Department added."); clear(); refresh(); }
            else UIHelper.err("Insert failed.");
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }

    private void update() {
        int r = table.getSelectedRow();
        if (r < 0) { UIHelper.err("Select a row."); return; }
        try {
            Department d = new Department();
            d.setDeptId((int) model.getValueAt(r, 0));
            d.setDeptName(nameF.getText().trim());
            d.setDeptCode(codeF.getText().trim().toUpperCase());
            d.setBuilding(bldgF.getText().trim());
            d.setFloorAreaSqm(Validation.isDecimal(areaF.getText()) ? new BigDecimal(areaF.getText().trim()) : BigDecimal.ZERO);
            d.setStaffCount(Validation.isInt(staffF.getText()) ? Integer.parseInt(staffF.getText().trim()) : 0);
            if (dao.update(d)) { System.out.println("Updated."); refresh(); }
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }

    private void delete() {
        int r = table.getSelectedRow();
        if (r < 0) { UIHelper.err("Select a row."); return; }
        int id = (int) model.getValueAt(r, 0);
        if (JOptionPane.showConfirmDialog(this, "Delete department?", "Confirm", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try { if (dao.delete(id)) { System.out.println("Deleted."); clear(); refresh(); } } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }

    private void search() {
        try {
            List<Department> list = dao.search(searchF.getText().trim());
            fill(list);
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }

    private void clear() { nameF.setText(""); codeF.setText(""); bldgF.setText(""); areaF.setText(""); staffF.setText(""); table.clearSelection(); }

    private void loadRow() {
        int r = table.getSelectedRow();
        if (r < 0) return;
        nameF.setText((String) model.getValueAt(r, 1));
        codeF.setText((String) model.getValueAt(r, 2));
        bldgF.setText((String) model.getValueAt(r, 3));
        areaF.setText(String.valueOf(model.getValueAt(r, 4)));
        staffF.setText(String.valueOf(model.getValueAt(r, 5)));
    }

    private void fill(List<Department> list) {
        model.setRowCount(0);
        for (Department d : list) model.addRow(new Object[]{d.getDeptId(), d.getDeptName(), d.getDeptCode(), d.getBuilding(), d.getFloorAreaSqm(), d.getStaffCount()});
    }

    @Override public void refresh() {
        try { fill(dao.findAll()); } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }
}
