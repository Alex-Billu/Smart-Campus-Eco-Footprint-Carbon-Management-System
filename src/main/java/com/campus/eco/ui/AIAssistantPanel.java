package com.campus.eco.ui;

import com.campus.eco.service.AIEngineService;
import com.campus.eco.util.UIHelper;

import javax.swing.*;
import java.awt.*;

public class AIAssistantPanel extends JPanel implements Refreshable {
    private final JTextArea chat = new JTextArea();
    private final JTextField input = new JTextField();
    private final AIEngineService svc = new AIEngineService();

    public AIAssistantPanel() {
        setLayout(new BorderLayout(12, 12));
        setBackground(UIHelper.BG);
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        JLabel title = new JLabel("AI Sustainability Assistant");
        title.setFont(UIHelper.H1); title.setForeground(UIHelper.PRIMARY_DARK);
        add(title, BorderLayout.NORTH);

        chat.setEditable(false);
        chat.setFont(UIHelper.BODY);
        chat.setBackground(UIHelper.CARD);
        chat.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(225,230,226),1), BorderFactory.createEmptyBorder(12,14,12,14)));
        chat.setLineWrap(true); chat.setWrapStyleWord(true);
        append("AI Assistant: Hello! Ask me about carbon reduction, electricity usage, water saving, waste management, or tree offset estimates.");
        add(new JScrollPane(chat), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(8, 8));
        bottom.setBackground(UIHelper.BG);
        input.setFont(UIHelper.BODY);
        input.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(210,220,212),1), BorderFactory.createEmptyBorder(8,10,8,10)));
        JButton send = new JButton("Send"); UIHelper.styleButton(send);
        send.addActionListener(e -> ask());
        input.addActionListener(e -> ask());
        bottom.add(input, BorderLayout.CENTER);
        bottom.add(send, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);

        JPanel hints = new JPanel(new FlowLayout(FlowLayout.LEFT)); hints.setBackground(UIHelper.BG);
        String[] qs = {"How can CSE reduce emissions?", "How many trees needed to offset emissions?", "Why did electricity increase?", "Best way to reduce waste?"};
        for (String q : qs) {
            JButton b = new JButton(q); UIHelper.styleSecondaryButton(b);
            b.addActionListener(e -> { input.setText(q); ask(); });
            hints.add(b);
        }
        add(hints, BorderLayout.NORTH);
    }

    private void ask() {
        String q = input.getText().trim();
        if (q.isEmpty()) return;
        append("\nYou: " + q);
        try {
            append("\nAI: " + svc.answer(q));
        } catch (Exception ex) {
            append("\nAI: Sorry, I couldn't process that. " + ex.getMessage());
        }
        input.setText("");
    }

    private void append(String s) { chat.append(s + "\n"); chat.setCaretPosition(chat.getDocument().getLength()); }

    @Override public void refresh() {}
}
