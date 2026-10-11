package com.campus.eco.ui;

import com.campus.eco.dao.WasteDAO;
import com.campus.eco.model.WasteRecord;
import com.campus.eco.util.UIHelper;
import com.campus.eco.util.Validation;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class WastePanel extends UsagePanelBase {
    private final JTextField recF = new JTextField();
    private final JTextField nonRecF = new JTextField();
    private final WasteDAO dao = new WasteDAO();

    public WastePanel() {
        super(new String[]{"ID","Dept","Month","Recyclable (kg)","Non-Recyclable (kg)","CO2 (kg)"});
        JPanel f = new JPanel(new GridLayout(4, 2, 8, 8));
        f.add(new JLabel("Department:")); f.add(deptBox);
        f.add(new JLabel("Month (YYYY-MM):")); monthF.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"))); f.add(monthF);
        f.add(new JLabel("Recyclable (kg):")); f.add(recF);
        f.add(new JLabel("Non-Recyclable (kg):")); f.add(nonRecF);
        build("Waste Management", f);
    }

    @Override
    public void refresh() {
        loadDepartments();
        try {
            model.setRowCount(0);
            for (WasteRecord w : dao.findAll()) model.addRow(new Object[]{w.getWasteId(), w.getDeptId(), w.getUsageMonth(), w.getRecyclableKg(), w.getNonRecyclableKg(), w.getCo2EmissionKg()});
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }

    @Override
    protected void add() {
        if (deptBox.getSelectedItem() == null) { UIHelper.err("Select department."); return; }
        try {
            WasteRecord w = new WasteRecord();
            w.setDeptId(((com.campus.eco.model.Department) deptBox.getSelectedItem()).getDeptId());
            w.setUsageMonth(monthF.getText().trim());
            w.setRecyclableKg(Validation.isDecimal(recF.getText()) ? new BigDecimal(recF.getText().trim()) : BigDecimal.ZERO);
            w.setNonRecyclableKg(Validation.isDecimal(nonRecF.getText()) ? new BigDecimal(nonRecF.getText().trim()) : BigDecimal.ZERO);
            if (dao.insert(w)) { System.out.println("Waste entry added."); refresh(); }
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }

    @Override
    protected void deleteRow(int row) {
        int id = (int) model.getValueAt(row, 0);
        try { if (dao.delete(id)) { System.out.println("Deleted."); refresh(); } } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }
}
