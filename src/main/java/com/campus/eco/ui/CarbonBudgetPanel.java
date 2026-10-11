package com.campus.eco.ui;

import com.campus.eco.dao.CarbonBudgetDAO;
import com.campus.eco.dao.DepartmentDAO;
import com.campus.eco.model.CarbonBudget;
import com.campus.eco.model.Department;
import com.campus.eco.util.UIHelper;
import com.campus.eco.util.Validation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CarbonBudgetPanel extends JPanel implements Refreshable {
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"ID","Dept","Month","Limit (kg)","Used (kg)","Status"}, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
    private final JTable table = new JTable(model);
    private final JComboBox<Department> deptBox = new JComboBox<>();
    private final JTextField monthF = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));
    private final JTextField limitF = new JTextField();
    private final CarbonBudgetDAO dao = new CarbonBudgetDAO();

    public CarbonBudgetPanel() {
        setLayout(new BorderLayout(12, 12));
        setBackground(UIHelper.BG);
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        JLabel title = new JLabel("Carbon Budget Management");
        title.setFont(UIHelper.H1); title.setForeground(UIHelper.PRIMARY_DARK);
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        form.setBackground(UIHelper.CARD); form.setBorder(BorderFactory.createTitledBorder("Set Budget"));
        form.add(new JLabel("Department:")); form.add(deptBox);
        form.add(new JLabel("Month:")); form.add(monthF);
        form.add(new JLabel("Limit (kg CO2):")); form.add(limitF);

        JButton set = new JButton("Set Budget"); UIHelper.styleButton(set);
        set.addActionListener(e -> setBudget());
        JButton del = new JButton("Delete"); UIHelper.styleDangerButton(del);
        del.addActionListener(e -> { int r = table.getSelectedRow(); if (r>=0) { try { dao.delete((int)model.getValueAt(r,0)); refresh(); } catch(Exception ex){UIHelper.err(ex.getMessage());} } });
        JPanel bp = new JPanel(new FlowLayout(FlowLayout.LEFT)); bp.setBackground(UIHelper.BG); bp.add(set); bp.add(del);
        JPanel left = new JPanel(new BorderLayout()); left.setBackground(UIHelper.BG); left.setPreferredSize(new Dimension(340,0));
        left.add(form, BorderLayout.CENTER); left.add(bp, BorderLayout.SOUTH);
        add(left, BorderLayout.WEST);

        table.setRowHeight(26); table.setFont(UIHelper.BODY); table.getTableHeader().setFont(UIHelper.BODY);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void setBudget() {
        if (deptBox.getSelectedItem() == null || !Validation.isDecimal(limitF.getText())) { UIHelper.err("Select dept & enter limit."); return; }
        try {
            CarbonBudget b = new CarbonBudget();
            b.setDeptId(((Department) deptBox.getSelectedItem()).getDeptId());
            b.setUsageMonth(monthF.getText().trim());
            b.setBudgetLimitKg(new BigDecimal(limitF.getText().trim()));
            b.setUsedKg(BigDecimal.ZERO); b.setStatus("OK");
            if (dao.upsert(b)) { System.out.println("Budget set."); refresh(); }
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }

    @Override public void refresh() {
        try {
            deptBox.removeAllItems();
            for (Department d : new DepartmentDAO().findAll()) deptBox.addItem(d);
            model.setRowCount(0);
            for (CarbonBudget b : dao.findAll()) model.addRow(new Object[]{b.getBudgetId(), b.getDeptId(), b.getUsageMonth(), b.getBudgetLimitKg(), b.getUsedKg(), b.getStatus()});
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }
}
