package com.campus.eco.ui;

import com.campus.eco.dao.DepartmentDAO;
import com.campus.eco.model.Department;
import com.campus.eco.util.UIHelper;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Shared scaffolding for usage-entry panels (Energy/Water/Transport/Waste).
 */
import javax.swing.table.DefaultTableModel;
public abstract class UsagePanelBase extends JPanel implements Refreshable {
    protected final DefaultTableModel model;
    protected final JTable table;
    protected final JComboBox<Department> deptBox = new JComboBox<>();
    protected final JTextField monthF = new JTextField();
    protected final JButton addBtn = new JButton("Add");
    protected final JButton delBtn = new JButton("Delete");

    protected UsagePanelBase(String[] columns) {
        setLayout(new BorderLayout(12, 12));
        setBackground(UIHelper.BG);
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        model = new DefaultTableModel(columns, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        table = new JTable(model);
        table.setRowHeight(26); table.setFont(UIHelper.BODY); table.getTableHeader().setFont(UIHelper.BODY);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    protected void build(String title, JPanel formPanel) {
        JLabel t = new JLabel(title);
        t.setFont(UIHelper.H1); t.setForeground(UIHelper.PRIMARY_DARK);
        add(t, BorderLayout.NORTH);

        JPanel left = new JPanel(new BorderLayout(8, 8));
        left.setBackground(UIHelper.BG);
        left.setPreferredSize(new Dimension(340, 0));
        formPanel.setBackground(UIHelper.CARD);
        formPanel.setBorder(BorderFactory.createTitledBorder("Entry"));
        left.add(formPanel, BorderLayout.CENTER);
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btns.setBackground(UIHelper.BG);
        UIHelper.styleButton(addBtn); UIHelper.styleDangerButton(delBtn);
        btns.add(addBtn); btns.add(delBtn);
        left.add(btns, BorderLayout.SOUTH);

        add(left, BorderLayout.WEST);
        add(new JScrollPane(table), BorderLayout.CENTER);

        addBtn.addActionListener(e -> add());
        delBtn.addActionListener(e -> { int r = table.getSelectedRow(); if (r >= 0) deleteRow(r); });
    }

    protected void loadDepartments() {
        try {
            deptBox.removeAllItems();
            List<Department> list = new DepartmentDAO().findAll();
            for (Department d : list) deptBox.addItem(d);
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }

    protected abstract void add();
    protected abstract void deleteRow(int row);
}
