package com.campus.eco.ui;

import com.campus.eco.model.Admin;
import com.campus.eco.util.UIHelper;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainFrame extends JFrame {
    private final Admin admin;
    private final JPanel content = new JPanel(new CardLayout());
    private final Map<String, JButton> navButtons = new LinkedHashMap<>();

    public MainFrame(Admin admin) {
        this.admin = admin;
        setTitle("Smart Campus Eco-Footprint & Carbon Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 800);
        setMinimumSize(new Dimension(1100, 700));
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIHelper.BG);

        // Sidebar
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(UIHelper.PRIMARY_DARK);
        sidebar.setPreferredSize(new Dimension(240, 0));

        JPanel logo = new JPanel(new BorderLayout());
        logo.setBackground(UIHelper.PRIMARY_DARK);
        logo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel l = new JLabel("EcoCampus", SwingConstants.LEFT);
        l.setForeground(Color.WHITE); l.setFont(UIHelper.H1);
        JLabel sub = new JLabel("Carbon Management");
        sub.setForeground(new Color(170, 210, 190)); sub.setFont(UIHelper.SMALL);
        logo.add(l, BorderLayout.CENTER); logo.add(sub, BorderLayout.SOUTH);
        sidebar.add(logo, BorderLayout.NORTH);

        JPanel nav = new JPanel();
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setBackground(UIHelper.PRIMARY_DARK);
        nav.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] items = {
                "Dashboard", "Departments", "Energy", "Water", "Transport", "Waste",
                "Carbon Calculator", "Eco Score", "Green Points", "Carbon Budget",
                "Trees", "Alerts", "Leaderboard", "Simulator", "Weather", "Cost vs Carbon",
                "Green Events", "AI Assistant", "QR Monitor", "Reports", "Certificates"
        };
        for (String it : items) {
            JButton b = new JButton(it);
            b.setHorizontalAlignment(SwingConstants.LEFT);
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            b.setBackground(UIHelper.PRIMARY_DARK);
            b.setForeground(Color.WHITE);
            b.setFont(UIHelper.BODY);
            b.setFocusPainted(false);
            b.setBorderPainted(false);
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            b.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
            b.addActionListener(e -> select(it));
            nav.add(b);
            nav.add(Box.createVerticalStrut(2));
            navButtons.put(it, b);
        }
        sidebar.add(new JScrollPane(nav), BorderLayout.CENTER);

        JPanel foot = new JPanel(new BorderLayout());
        foot.setBackground(UIHelper.PRIMARY_DARK);
        foot.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        JLabel user = new JLabel("  " + (admin.getFullName() != null ? admin.getFullName() : admin.getUsername()));
        user.setForeground(new Color(190, 220, 200)); user.setFont(UIHelper.SMALL);
        JButton logout = new JButton("Logout");
        logout.setForeground(Color.WHITE); logout.setBackground(new Color(192, 57, 43));
        logout.setFocusPainted(false); logout.setBorderPainted(false);
        logout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logout.addActionListener(e -> { dispose(); new LoginFrame().setVisible(true); });
        foot.add(user, BorderLayout.CENTER); foot.add(logout, BorderLayout.EAST);
        sidebar.add(foot, BorderLayout.SOUTH);

        // Content
        content.setBackground(UIHelper.BG);
        addPanel("Dashboard", new DashboardPanel());
        addPanel("Departments", new DepartmentPanel());
        addPanel("Energy", new EnergyPanel());
        addPanel("Water", new WaterPanel());
        addPanel("Transport", new TransportPanel());
        addPanel("Waste", new WastePanel());
        addPanel("Carbon Calculator", new CarbonCalculatorPanel());
        addPanel("Eco Score", new SustainabilityPanel());
        addPanel("Green Points", new GreenPointsPanel());
        addPanel("Carbon Budget", new CarbonBudgetPanel());
        addPanel("Trees", new TreePanel());
        addPanel("Alerts", new AlertPanel());
        addPanel("Leaderboard", new LeaderboardPanel());
        addPanel("Simulator", new SimulatorPanel());
        addPanel("Weather", new WeatherPanel());
        addPanel("Cost vs Carbon", new CostAnalysisPanel());
        addPanel("Green Events", new GreenEventPanel());
        addPanel("AI Assistant", new AIAssistantPanel());
        addPanel("QR Monitor", new QRPanel());
        addPanel("Reports", new ReportPanel());
        addPanel("Certificates", new CertificatePanel());

        root.add(sidebar, BorderLayout.WEST);
        root.add(content, BorderLayout.CENTER);
        setContentPane(root);
        select("Dashboard");
    }

    private void addPanel(String name, JPanel panel) {
        JScrollPane sp = new JScrollPane(panel);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.setBackground(UIHelper.BG);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        content.add(sp, name);
    }

    private void select(String name) {
        ((CardLayout) content.getLayout()).show(content, name);
        for (Map.Entry<String, JButton> e : navButtons.entrySet()) {
            boolean active = e.getKey().equals(name);
            e.getValue().setBackground(active ? UIHelper.PRIMARY : UIHelper.PRIMARY_DARK);
            e.getValue().setFont(active ? UIHelper.H2 : UIHelper.BODY);
        }
        if (content.getComponentCount() > 0) {
            for (Component c : content.getComponents()) {
                if (c instanceof JScrollPane sp && sp.getViewport().getView() instanceof Refreshable r) {
                    r.refresh();
                }
            }
        }
        setTitle("Smart Campus Eco-Footprint - " + name);
    }
}
