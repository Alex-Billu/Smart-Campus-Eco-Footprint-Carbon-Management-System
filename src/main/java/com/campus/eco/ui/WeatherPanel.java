package com.campus.eco.ui;

import com.campus.eco.model.WeatherImpact;
import com.campus.eco.service.WeatherImpactService;
import com.campus.eco.util.UIHelper;
import com.campus.eco.util.Validation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class WeatherPanel extends JPanel implements Refreshable {
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"Month","Temp (C)","Rain (mm)","Humidity %","Season","Energy Trend","Water Trend"}, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
    private final JTable table = new JTable(model);
    private final JTextField monthF = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));
    private final JTextField tempF = new JTextField(), rainF = new JTextField(), humF = new JTextField();
    private final JLabel forecastL = new JLabel("--");
    private final WeatherImpactService svc = new WeatherImpactService();

    public WeatherPanel() {
        setLayout(new BorderLayout(12, 12));
        setBackground(UIHelper.BG);
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        JLabel title = new JLabel("Smart Weather Impact Analysis");
        title.setFont(UIHelper.H1); title.setForeground(UIHelper.PRIMARY_DARK);
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.setBackground(UIHelper.CARD); form.setBorder(BorderFactory.createTitledBorder("Weather Entry"));
        form.add(new JLabel("Month:")); form.add(monthF);
        form.add(new JLabel("Temperature (C):")); form.add(tempF);
        form.add(new JLabel("Rainfall (mm):")); form.add(rainF);
        form.add(new JLabel("Humidity (%):")); form.add(humF);

        JButton add = new JButton("Analyze & Save"); UIHelper.styleButton(add);
        add.addActionListener(e -> add());
        JButton fc = new JButton("Forecast"); UIHelper.styleSecondaryButton(fc);
        fc.addActionListener(e -> forecast());
        JPanel bp = new JPanel(new FlowLayout(FlowLayout.LEFT)); bp.setBackground(UIHelper.BG); bp.add(add); bp.add(fc);
        JPanel left = new JPanel(new BorderLayout()); left.setBackground(UIHelper.BG); left.setPreferredSize(new Dimension(340,0));
        left.add(form, BorderLayout.CENTER); left.add(bp, BorderLayout.SOUTH);
        add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new BorderLayout(8, 8)); right.setBackground(UIHelper.BG);
        table.setRowHeight(26); table.setFont(UIHelper.BODY); table.getTableHeader().setFont(UIHelper.BODY);
        right.add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel fcPanel = new JPanel(new BorderLayout()); fcPanel.setBackground(UIHelper.CARD);
        fcPanel.setBorder(BorderFactory.createTitledBorder("Summer Energy Forecast"));
        forecastL.setFont(UIHelper.H2); forecastL.setForeground(UIHelper.PRIMARY_DARK);
        fcPanel.add(forecastL, BorderLayout.CENTER);
        fcPanel.setPreferredSize(new Dimension(0, 60));
        right.add(fcPanel, BorderLayout.SOUTH);
        add(right, BorderLayout.CENTER);
    }

    private void add() {
        if (!Validation.isDecimal(tempF.getText()) || !Validation.isDecimal(rainF.getText()) || !Validation.isDecimal(humF.getText())) { UIHelper.err("Enter valid numbers."); return; }
        try {
            WeatherImpact w = svc.analyze(monthF.getText().trim(), new BigDecimal(tempF.getText().trim()), new BigDecimal(rainF.getText().trim()), new BigDecimal(humF.getText().trim()));
            if (svc.persist(w)) { System.out.println("Weather analysis saved."); refresh(); }
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }

    private void forecast() {
        try {
            List<WeatherImpact> all = svc.all();
            forecastL.setText(svc.forecastSummerEnergy(all));
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }

    @Override public void refresh() {
        try {
            model.setRowCount(0);
            for (WeatherImpact w : svc.all()) model.addRow(new Object[]{w.getUsageMonth(), w.getTemperatureC(), w.getRainfallMm(), w.getHumidityPct(), w.getSeason(), w.getEnergyTrend(), w.getWaterTrend()});
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }
}
