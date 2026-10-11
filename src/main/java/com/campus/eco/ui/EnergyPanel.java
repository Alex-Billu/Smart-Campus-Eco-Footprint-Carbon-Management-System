package com.campus.eco.ui;

import com.campus.eco.dao.EnergyDAO;
import com.campus.eco.model.EnergyUsage;
import com.campus.eco.util.UIHelper;
import com.campus.eco.util.Validation;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EnergyPanel extends UsagePanelBase {
    private final JTextField kwhF = new JTextField();
    private final JTextField costF = new JTextField();
    private final EnergyDAO dao = new EnergyDAO();

    public EnergyPanel() {
        super(new String[]{"ID","Dept","Month","kWh","Cost","CO2 (kg)"});
        JPanel f = new JPanel(new GridLayout(4, 2, 8, 8));
        f.add(new JLabel("Department:")); f.add(deptBox);
        f.add(new JLabel("Month (YYYY-MM):")); monthF.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"))); f.add(monthF);
        f.add(new JLabel("Electricity (kWh):")); f.add(kwhF);
        f.add(new JLabel("Cost (₹):")); f.add(costF);
        build("Energy Management", f);
    }

    @Override
    public void refresh() {
        loadDepartments();
        try {
            model.setRowCount(0);
            for (EnergyUsage e : dao.findAll()) model.addRow(new Object[]{e.getEnergyId(), e.getDeptId(), e.getUsageMonth(), e.getElectricityKwh(), e.getCostAmount(), e.getCo2EmissionKg()});
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }

    @Override
    protected void add() {
        if (deptBox.getSelectedItem() == null) { UIHelper.err("Select department."); return; }
        if (!Validation.isDecimal(kwhF.getText())) { UIHelper.err("Enter valid kWh."); return; }
        try {
            EnergyUsage e = new EnergyUsage();
            e.setDeptId(((com.campus.eco.model.Department) deptBox.getSelectedItem()).getDeptId());
            e.setUsageMonth(monthF.getText().trim());
            e.setElectricityKwh(new BigDecimal(kwhF.getText().trim()));
            e.setCostAmount(Validation.isDecimal(costF.getText()) ? new BigDecimal(costF.getText().trim()) : BigDecimal.ZERO);
            if (dao.insert(e)) { System.out.println("Energy entry added."); refresh(); }
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }

    @Override
    protected void deleteRow(int row) {
        int id = (int) model.getValueAt(row, 0);
        try { if (dao.delete(id)) { System.out.println("Deleted."); refresh(); } } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }
}
