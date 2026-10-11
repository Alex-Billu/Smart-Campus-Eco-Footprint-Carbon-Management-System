package com.campus.eco.ui;

import com.campus.eco.dao.DepartmentDAO;
import com.campus.eco.dao.QRTrackingDAO;
import com.campus.eco.model.Department;
import com.campus.eco.model.QRTracking;
import com.campus.eco.service.QRService;
import com.campus.eco.util.UIHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class QRPanel extends JPanel implements Refreshable {
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"ID","Dept","Resource","Data"}, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
    private final JTable table = new JTable(model);
    private final JComboBox<Department> deptBox = new JComboBox<>();
    private final JTextField resF = new JTextField();
    private final JTextField dataF = new JTextField();
    private final JLabel qrLabel = new JLabel("QR preview", SwingConstants.CENTER);
    private final QRTrackingDAO dao = new QRTrackingDAO();

    public QRPanel() {
        setLayout(new BorderLayout(12, 12));
        setBackground(UIHelper.BG);
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        JLabel title = new JLabel("QR-Based Resource Monitoring");
        title.setFont(UIHelper.H1); title.setForeground(UIHelper.PRIMARY_DARK);
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        form.setBackground(UIHelper.CARD); form.setBorder(BorderFactory.createTitledBorder("Generate QR"));
        form.add(new JLabel("Department:")); form.add(deptBox);
        form.add(new JLabel("Resource Type:")); form.add(resF);
        form.add(new JLabel("Data (e.g. meter reading):")); form.add(dataF);

        JButton gen = new JButton("Generate & Save"); UIHelper.styleButton(gen);
        gen.addActionListener(e -> generate());
        JPanel bp = new JPanel(new FlowLayout(FlowLayout.LEFT)); bp.setBackground(UIHelper.BG); bp.add(gen);
        JPanel left = new JPanel(new BorderLayout()); left.setBackground(UIHelper.BG); left.setPreferredSize(new Dimension(340,0));
        left.add(form, BorderLayout.CENTER); left.add(bp, BorderLayout.SOUTH);
        add(left, BorderLayout.WEST);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(table), qrPreview());
        split.setDividerLocation(500);
        add(split, BorderLayout.CENTER);
        table.setRowHeight(26); table.setFont(UIHelper.BODY); table.getTableHeader().setFont(UIHelper.BODY);
    }

    private JPanel qrPreview() {
        JPanel p = new JPanel(new BorderLayout()); p.setBackground(UIHelper.CARD);
        p.setBorder(BorderFactory.createTitledBorder("QR Preview"));
        qrLabel.setPreferredSize(new Dimension(260, 260));
        p.add(qrLabel, BorderLayout.CENTER);
        return p;
    }

    private void generate() {
        if (deptBox.getSelectedItem() == null || dataF.getText().trim().isEmpty()) { UIHelper.err("Select dept & enter data."); return; }
        try {
            Department d = (Department) deptBox.getSelectedItem();
            String payload = "DEPT:" + d.getDeptCode() + "|RES:" + resF.getText().trim() + "|DATA:" + dataF.getText().trim();
            QRTracking q = new QRTracking();
            q.setDeptId(d.getDeptId());
            q.setResourceType(resF.getText().trim());
            q.setEncodedData(payload);
            if (dao.insert(q)) {
                BufferedImage img = QRService.generate(payload);
                qrLabel.setIcon(new ImageIcon(img));
                qrLabel.setText(null);
                System.out.println("QR generated & saved.");
                refresh();
            }
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }

    @Override public void refresh() {
        try {
            deptBox.removeAllItems();
            for (Department d : new DepartmentDAO().findAll()) deptBox.addItem(d);
            model.setRowCount(0);
            for (QRTracking q : dao.findAll()) model.addRow(new Object[]{q.getQrId(), q.getDeptId(), q.getResourceType(), q.getEncodedData()});
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }
}
