package com.campus.eco.ui;

import com.campus.eco.dao.WaterDAO;
import com.campus.eco.model.WaterUsage;
import com.campus.eco.util.UIHelper;
import com.campus.eco.util.Validation;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class WaterPanel extends UsagePanelBase {
    private final JTextField litresF = new JTextField();
    private final JTextField costF = new JTextField();
    private final WaterDAO dao = new WaterDAO();

    public WaterPanel() {
        super(new String[]{"ID","Dept","Month","Litres","Cost","CO2 (kg)"});
        JPanel f = new JPanel(new GridLayout(4, 2, 8, 8));
        f.add(new JLabel("Department:")); f.add(deptBox);
        f.add(new JLabel("Month (YYYY-MM):")); monthF.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"))); f.add(monthF);
        f.add(new JLabel("Water (Litres):")); f.add(litresF);
        f.add(new JLabel("Cost (₹):")); f.add(costF);
        build("Water Management", f);
    }

    @Override
    public void refresh() {
        loadDepartments();
        try {
            model.setRowCount(0);
            for (WaterUsage w : dao.findAll()) model.addRow(new Object[]{w.getWaterId(), w.getDeptId(), w.getUsageMonth(), w.getWaterLitres(), w.getCostAmount(), w.getCo2EmissionKg()});
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }

    @Override
    protected void add() {
        if (deptBox.getSelectedItem() == null) { UIHelper.err("Select department."); return; }
        if (!Validation.isDecimal(litresF.getText())) { UIHelper.err("Enter valid litres."); return; }
        try {
            WaterUsage w = new WaterUsage();
            w.setDeptId(((com.campus.eco.model.Department) deptBox.getSelectedItem()).getDeptId());
            w.setUsageMonth(monthF.getText().trim());
            w.setWaterLitres(new BigDecimal(litresF.getText().trim()));
            w.setCostAmount(Validation.isDecimal(costF.getText()) ? new BigDecimal(costF.getText().trim()) : BigDecimal.ZERO);
            if (dao.insert(w)) { System.out.println("Water entry added."); refresh(); }
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }

    @Override
    protected void deleteRow(int row) {
        int id = (int) model.getValueAt(row, 0);
        try { if (dao.delete(id)) { System.out.println("Deleted."); refresh(); } } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }
}
