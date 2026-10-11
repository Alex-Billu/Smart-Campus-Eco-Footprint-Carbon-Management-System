package com.campus.eco.ui;

import com.campus.eco.config.DBConnection;
import com.campus.eco.dao.AdminDAO;
import com.campus.eco.model.Admin;
import com.campus.eco.util.UIHelper;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final JTextField userField = new JTextField();
    private final JPasswordField passField = new JPasswordField();
    private final JButton loginBtn = new JButton("Login");
    private final JButton forgotBtn = new JButton("Forgot Password?");

    public LoginFrame() {
        setTitle("Smart Campus Eco-Footprint - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(440, 560);
        setLocationRelativeTo(null);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIHelper.PRIMARY_DARK);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIHelper.PRIMARY_DARK);
        header.setPreferredSize(new Dimension(440, 180));
        header.setBorder(BorderFactory.createEmptyBorder(30, 30, 20, 30));
        JLabel icon = new JLabel("\u263B", SwingConstants.CENTER);
        icon.setForeground(Color.WHITE);
        icon.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        JLabel title = new JLabel("Smart Campus Eco", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(UIHelper.H1);
        JLabel sub = new JLabel("Footprint & Carbon Management", SwingConstants.CENTER);
        sub.setForeground(new Color(180, 220, 200));
        sub.setFont(UIHelper.SMALL);
        JPanel t = new JPanel(new GridLayout(2, 1));
        t.setBackground(UIHelper.PRIMARY_DARK);
        t.add(title); t.add(sub);
        header.add(icon, BorderLayout.CENTER);
        header.add(t, BorderLayout.SOUTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UIHelper.BG);
        form.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 0, 8, 0);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;

        JLabel u = new JLabel("Username"); u.setFont(UIHelper.BODY); u.setForeground(UIHelper.TEXT);
        g.gridx = 0; g.gridy = 0; form.add(u, g);
        styleField(userField); g.gridy = 1; form.add(userField, g);

        JLabel p = new JLabel("Password"); p.setFont(UIHelper.BODY); p.setForeground(UIHelper.TEXT);
        g.gridy = 2; form.add(p, g);
        styleField(passField); g.gridy = 3; form.add(passField, g);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btns.setBackground(UIHelper.BG);
        UIHelper.styleButton(loginBtn);
        UIHelper.styleSecondaryButton(forgotBtn);
        btns.add(loginBtn); btns.add(forgotBtn);
        g.gridy = 4; g.insets = new Insets(20, 0, 0, 0); form.add(btns, g);

        JLabel hint = new JLabel("Default: admin / admin123", SwingConstants.CENTER);
        hint.setFont(UIHelper.SMALL); hint.setForeground(UIHelper.MUTED);
        g.gridy = 5; g.insets = new Insets(10, 0, 0, 0); form.add(hint, g);

        root.add(header, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
        setContentPane(root);

        loginBtn.addActionListener(e -> doLogin());
        forgotBtn.addActionListener(e -> doForgot());
        passField.addActionListener(e -> doLogin());
        getRootPane().setDefaultButton(loginBtn);
    }

    private void styleField(JTextField f) {
        f.setFont(UIHelper.BODY);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 220, 212), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
    }

    private void doLogin() {
        String u = userField.getText().trim();
        String p = new String(passField.getPassword());
        if (u.isEmpty() || p.isEmpty()) { UIHelper.err("Enter username and password."); return; }
        try {
            if (!DBConnection.testConnection()) { UIHelper.err("Cannot connect to MySQL. Check DB and credentials."); return; }
            Admin a = new AdminDAO().login(u, p);
            if (a == null) { UIHelper.err("Invalid credentials."); return; }
            MainFrame mf = new MainFrame(a);
            mf.setVisible(true);
            dispose();
        } catch (Exception ex) { UIHelper.err("Login error: " + ex.getMessage()); }
    }

    private void doForgot() {
        String u = JOptionPane.showInputDialog(this, "Enter your username:");
        if (u == null || u.trim().isEmpty()) return;
        String np = JOptionPane.showInputDialog(this, "Enter new password:");
        if (np == null || np.trim().isEmpty()) return;
        try {
            if (!new AdminDAO().usernameExists(u)) { UIHelper.err("Username not found."); return; }
            if (new AdminDAO().changePassword(u, np.trim())) System.out.println("Password reset. Login now.");
            else UIHelper.err("Reset failed.");
        } catch (Exception ex) { UIHelper.err(ex.getMessage()); }
    }
}
