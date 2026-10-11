package com.campus.eco.ui;

import com.campus.eco.dao.AlertLogDAO;
import com.campus.eco.model.AlertLog;
import com.campus.eco.service.AlertService;
import com.campus.eco.util.UIHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AlertPanel extends JPanel implements Refreshable {
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"ID","Dept","Type","Severity","Month","Message"}, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
    private final JTable table = new JTable(model);
    private final JTextField monthF = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));
    private final AlertLogDAO dao = new AlertLogDAO();

    public AlertPanel() {
        setLayout(new BorderLayout(12, 12));
        setBackground(UIHelper.BG);
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        JLabel title = new JLabel("Smart Alert System");
        title.setFont(UIHelper.H1); title.setForeground(UIHelper.PRIMARY_DARK);
        add(title, BorderLayout.NORTH);

        JPanel ctrl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ctrl.setBackground(UIHelper.BG);
        ctrl.add(new JLabel("Month:")); ctrl.add(monthF);
        JButton scan = new JButton("Scan & Generate Alerts"); UIHelper.styleButton(scan);
        scan.addActionListener(e -> scan());
        ctrl.add(scan);
        add(ctrl, BorderLayout.NORTH);

        table.setRowHeight(28); table.setFont(UIHelper.BODY); table.getTableHeader().setFont(UIHelper.BODY);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void scan() {
        try {
            List<AlertLog> created = new AlertService().scanAndPersist(monthF.getText().trim());
            refresh();
            System.out.println(created.size() + " alerts generated.");
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }

    @Override public void refresh() {
        try {
            model.setRowCount(0);
            for (AlertLog a : dao.findAll()) model.addRow(new Object[]{a.getAlertId(), a.getDeptId(), a.getAlertType(), a.getSeverity(), a.getUsageMonth(), a.getMessage()});
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }
}
