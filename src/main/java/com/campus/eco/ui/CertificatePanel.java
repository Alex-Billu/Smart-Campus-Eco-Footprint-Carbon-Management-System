package com.campus.eco.ui;

import com.campus.eco.dao.CertificateDAO;
import com.campus.eco.dao.DepartmentDAO;
import com.campus.eco.model.CertificateRecord;
import com.campus.eco.model.Department;
import com.campus.eco.util.UIHelper;
import com.campus.eco.util.Validation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CertificatePanel extends JPanel implements Refreshable {
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"ID","Dept","Type","Month","Title"}, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
    private final JTable table = new JTable(model);
    private final JComboBox<Department> deptBox = new JComboBox<>();
    private final JTextField typeF = new JTextField(), monthF = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"))), titleF = new JTextField(), descF = new JTextField();
    private final CertificateDAO dao = new CertificateDAO();

    public CertificatePanel() {
        setLayout(new BorderLayout(12, 12));
        setBackground(UIHelper.BG);
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        JLabel title = new JLabel("Certificate Records");
        title.setFont(UIHelper.H1); title.setForeground(UIHelper.PRIMARY_DARK);
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.setBackground(UIHelper.CARD); form.setBorder(BorderFactory.createTitledBorder("Issue Certificate"));
        form.add(new JLabel("Department:")); form.add(deptBox);
        form.add(new JLabel("Type (Green Champion/Eco Star):")); form.add(typeF);
        form.add(new JLabel("Month:")); form.add(monthF);
        form.add(new JLabel("Title:")); form.add(titleF);
        form.add(new JLabel("Description:")); form.add(descF);

        JButton add = new JButton("Issue"); UIHelper.styleButton(add);
        add.addActionListener(e -> issue());
        JPanel bp = new JPanel(new FlowLayout(FlowLayout.LEFT)); bp.setBackground(UIHelper.BG); bp.add(add);
        JPanel left = new JPanel(new BorderLayout()); left.setBackground(UIHelper.BG); left.setPreferredSize(new Dimension(340,0));
        left.add(form, BorderLayout.CENTER); left.add(bp, BorderLayout.SOUTH);
        add(left, BorderLayout.WEST);

        table.setRowHeight(26); table.setFont(UIHelper.BODY); table.getTableHeader().setFont(UIHelper.BODY);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void issue() {
        if (deptBox.getSelectedItem() == null || !Validation.notBlank(titleF.getText())) { UIHelper.err("Select dept & enter title."); return; }
        try {
            CertificateRecord c = new CertificateRecord();
            c.setDeptId(((Department) deptBox.getSelectedItem()).getDeptId());
            c.setCertType(typeF.getText().trim());
            c.setCertMonth(monthF.getText().trim());
            c.setTitle(titleF.getText().trim());
            c.setDescription(descF.getText().trim());
            if (dao.insert(c)) { System.out.println("Certificate issued."); refresh(); }
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }

    @Override public void refresh() {
        try {
            deptBox.removeAllItems();
            for (Department d : new DepartmentDAO().findAll()) deptBox.addItem(d);
            model.setRowCount(0);
            for (CertificateRecord c : dao.findAll()) model.addRow(new Object[]{c.getCertId(), c.getDeptId(), c.getCertType(), c.getCertMonth(), c.getTitle()});
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }
}
