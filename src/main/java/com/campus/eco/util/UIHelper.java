package com.campus.eco.util;

import javax.swing.*;
import java.awt.*;

public class UIHelper {
    public static final Color PRIMARY = new Color(34, 139, 87);
    public static final Color PRIMARY_DARK = new Color(22, 100, 62);
    public static final Color ACCENT = new Color(245, 200, 66);
    public static final Color BG = new Color(244, 247, 244);
    public static final Color CARD = Color.WHITE;
    public static final Color TEXT = new Color(33, 41, 52);
    public static final Color MUTED = new Color(120, 130, 140);
    public static final Color DANGER = new Color(214, 64, 64);
    public static final Color WARN = new Color(232, 160, 40);
    public static final Color SUCCESS = new Color(46, 160, 90);

    public static final Font H1 = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font H2 = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font SMALL = new Font("Segoe UI", Font.PLAIN, 11);

    public static void styleButton(JButton b) {
        b.setBackground(PRIMARY);
        b.setForeground(Color.WHITE);
        b.setFont(BODY);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(120, 34));
    }

    public static void styleSecondaryButton(JButton b) {
        b.setBackground(new Color(235, 240, 236));
        b.setForeground(PRIMARY_DARK);
        b.setFont(BODY);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(120, 34));
    }

    public static void styleDangerButton(JButton b) {
        b.setBackground(DANGER);
        b.setForeground(Color.WHITE);
        b.setFont(BODY);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(120, 34));
    }

    public static JPanel card(String title) {
        JPanel p = new JPanel();
        p.setBackground(CARD);
        p.setLayout(new BorderLayout(8, 8));
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 230, 226), 1),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)));
        if (title != null) {
            JLabel l = new JLabel(title);
            l.setFont(H2);
            l.setForeground(PRIMARY_DARK);
            p.add(l, BorderLayout.NORTH);
        }
        return p;
    }

    public static void msg(String s) { JOptionPane.showMessageDialog(null, s); }
    public static void err(String s) { JOptionPane.showMessageDialog(null, s, "Error", JOptionPane.ERROR_MESSAGE); }
}
