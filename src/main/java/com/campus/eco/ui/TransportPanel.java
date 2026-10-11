package com.campus.eco.ui;

import com.campus.eco.dao.TransportDAO;
import com.campus.eco.model.TransportDetails;
import com.campus.eco.util.UIHelper;
import com.campus.eco.util.Validation;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TransportPanel extends UsagePanelBase {
    private final JTextField vehicleF = new JTextField();
    private final JTextField distanceF = new JTextField();
    private final JTextField fuelF = new JTextField();
    private final TransportDAO dao = new TransportDAO();

    public TransportPanel() {
        super(new String[]{"ID","Dept","Month","Vehicle","Distance (km)","Fuel (L)","CO2 (kg)"});
        JPanel f = new JPanel(new GridLayout(5, 2, 8, 8));
        f.add(new JLabel("Department:")); f.add(deptBox);
        f.add(new JLabel("Month (YYYY-MM):")); monthF.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"))); f.add(monthF);
        f.add(new JLabel("Vehicle Type:")); f.add(vehicleF);
        f.add(new JLabel("Distance (km):")); f.add(distanceF);
        f.add(new JLabel("Fuel (Litres):")); f.add(fuelF);
        build("Transportation Management", f);
    }

    @Override
    public void refresh() {
        loadDepartments();
        try {
            model.setRowCount(0);
            for (TransportDetails t : dao.findAll()) model.addRow(new Object[]{t.getTransportId(), t.getDeptId(), t.getUsageMonth(), t.getVehicleType(), t.getDistanceKm(), t.getFuelLitres(), t.getCo2EmissionKg()});
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }

    @Override
    protected void add() {
        if (deptBox.getSelectedItem() == null) { UIHelper.err("Select department."); return; }
        if (!Validation.isDecimal(fuelF.getText())) { UIHelper.err("Enter valid fuel litres."); return; }
        try {
            TransportDetails t = new TransportDetails();
            t.setDeptId(((com.campus.eco.model.Department) deptBox.getSelectedItem()).getDeptId());
            t.setUsageMonth(monthF.getText().trim());
            t.setVehicleType(vehicleF.getText().trim());
            t.setDistanceKm(Validation.isDecimal(distanceF.getText()) ? new BigDecimal(distanceF.getText().trim()) : BigDecimal.ZERO);
            t.setFuelLitres(new BigDecimal(fuelF.getText().trim()));
            if (dao.insert(t)) { System.out.println("Transport entry added."); refresh(); }
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }

    @Override
    protected void deleteRow(int row) {
        int id = (int) model.getValueAt(row, 0);
        try { if (dao.delete(id)) { System.out.println("Deleted."); refresh(); } } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }
}
