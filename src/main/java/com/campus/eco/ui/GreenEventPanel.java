package com.campus.eco.ui;

import com.campus.eco.dao.GreenEventDAO;
import com.campus.eco.model.EventParticipant;
import com.campus.eco.model.GreenEvent;
import com.campus.eco.util.UIHelper;
import com.campus.eco.util.Validation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class GreenEventPanel extends JPanel implements Refreshable {
    private final DefaultTableModel evModel = new DefaultTableModel(new String[]{"ID","Name","Type","Date","Location","Points"}, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
    private final JTable evTable = new JTable(evModel);
    private final DefaultTableModel partModel = new DefaultTableModel(new String[]{"ID","Name","Dept","Attended","Points"}, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
    private final JTable partTable = new JTable(partModel);
    private final JTextField nameF = new JTextField(), typeF = new JTextField(), dateF = new JTextField(LocalDate.now().toString()), locF = new JTextField(), ptsF = new JTextField();
    private final JTextField partNameF = new JTextField(), partDeptF = new JTextField();
    private final GreenEventDAO dao = new GreenEventDAO();

    public GreenEventPanel() {
        setLayout(new BorderLayout(12, 12));
        setBackground(UIHelper.BG);
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        JLabel title = new JLabel("Green Event Management");
        title.setFont(UIHelper.H1); title.setForeground(UIHelper.PRIMARY_DARK);
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.setBackground(UIHelper.CARD); form.setBorder(BorderFactory.createTitledBorder("Create Event"));
        form.add(new JLabel("Event Name:")); form.add(nameF);
        form.add(new JLabel("Type (Tree/Clean/Workshop):")); form.add(typeF);
        form.add(new JLabel("Date (YYYY-MM-DD):")); form.add(dateF);
        form.add(new JLabel("Location:")); form.add(locF);
        form.add(new JLabel("Green Points:")); form.add(ptsF);

        JButton add = new JButton("Create"); UIHelper.styleButton(add);
        add.addActionListener(e -> create());
        JButton del = new JButton("Delete"); UIHelper.styleDangerButton(del);
        del.addActionListener(e -> { int r = evTable.getSelectedRow(); if (r>=0) { try { dao.delete((int)evModel.getValueAt(r,0)); refresh(); } catch(Exception ex){UIHelper.err(ex.getMessage());} } });
        JPanel bp = new JPanel(new FlowLayout(FlowLayout.LEFT)); bp.setBackground(UIHelper.BG); bp.add(add); bp.add(del);
        JPanel left = new JPanel(new BorderLayout()); left.setBackground(UIHelper.BG); left.setPreferredSize(new Dimension(340,0));
        left.add(form, BorderLayout.CENTER); left.add(bp, BorderLayout.SOUTH);
        add(left, BorderLayout.WEST);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(evTable), participantPanel());
        split.setDividerLocation(220);
        add(split, BorderLayout.CENTER);
        evTable.getSelectionModel().addListSelectionListener(e -> { if (!e.getValueIsAdjusting() && evTable.getSelectedRow()>=0) loadParticipants(); });
        evTable.setRowHeight(26); evTable.setFont(UIHelper.BODY); evTable.getTableHeader().setFont(UIHelper.BODY);
        partTable.setRowHeight(26); partTable.setFont(UIHelper.BODY); partTable.getTableHeader().setFont(UIHelper.BODY);
    }

    private JPanel participantPanel() {
        JPanel p = new JPanel(new BorderLayout(8, 8)); p.setBackground(UIHelper.BG);
        JPanel f = new JPanel(new FlowLayout(FlowLayout.LEFT)); f.setBackground(UIHelper.BG);
        f.add(new JLabel("Participant:")); f.add(partNameF); partNameF.setPreferredSize(new Dimension(120, 28));
        f.add(new JLabel("Dept ID:")); f.add(partDeptF); partDeptF.setPreferredSize(new Dimension(60, 28));
        JButton addP = new JButton("Register"); UIHelper.styleSecondaryButton(addP);
        addP.addActionListener(e -> addParticipant());
        f.add(addP);
        p.add(f, BorderLayout.NORTH);
        p.add(new JScrollPane(partTable), BorderLayout.CENTER);
        return p;
    }

    private void create() {
        if (!Validation.notBlank(nameF.getText()) || !Validation.isInt(ptsF.getText())) { UIHelper.err("Enter name & points."); return; }
        try {
            GreenEvent e = new GreenEvent();
            e.setEventName(nameF.getText().trim());
            e.setEventType(typeF.getText().trim());
            e.setEventDate(LocalDate.parse(dateF.getText().trim()));
            e.setLocation(locF.getText().trim());
            e.setGreenPoints(Integer.parseInt(ptsF.getText().trim()));
            if (dao.insert(e)) { System.out.println("Event created."); refresh(); }
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }

    private void addParticipant() {
        int r = evTable.getSelectedRow();
        if (r < 0) { UIHelper.err("Select an event."); return; }
        try {
            EventParticipant p = new EventParticipant();
            p.setEventId((int) evModel.getValueAt(r, 0));
            p.setParticipantName(partNameF.getText().trim());
            p.setDeptId(Validation.isInt(partDeptF.getText()) ? Integer.parseInt(partDeptF.getText().trim()) : null);
            p.setAttended(true);
            p.setPointsAwarded((int) evModel.getValueAt(r, 5));
            if (dao.addParticipant(p)) { System.out.println("Registered."); loadParticipants(); }
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }

    private void loadParticipants() {
        int r = evTable.getSelectedRow();
        if (r < 0) return;
        try {
            partModel.setRowCount(0);
            for (EventParticipant p : dao.participants((int) evModel.getValueAt(r, 0)))
                partModel.addRow(new Object[]{p.getParticipantId(), p.getParticipantName(), p.getDeptId(), p.isAttended()?"Yes":"No", p.getPointsAwarded()});
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }

    @Override public void refresh() {
        try {
            evModel.setRowCount(0);
            for (GreenEvent e : dao.findAll()) evModel.addRow(new Object[]{e.getEventId(), e.getEventName(), e.getEventType(), e.getEventDate(), e.getLocation(), e.getGreenPoints()});
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }
}
