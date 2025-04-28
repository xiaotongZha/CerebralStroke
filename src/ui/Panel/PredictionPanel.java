package ui.Panel;

import ui.MainFrame;

import javax.swing.*;
import java.awt.*;

public class PredictionPanel extends JPanel {
    private MainFrame mainFrame;

    public PredictionPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("血肿扩张预警界面", JLabel.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));

        JButton backButton = new JButton("返回主菜单");
        backButton.addActionListener(e -> mainFrame.showMainMenuPanel());

        add(titleLabel, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);
    }
}