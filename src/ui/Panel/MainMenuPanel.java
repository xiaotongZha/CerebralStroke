package ui.Panel;

import ui.MainFrame;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    private MainFrame mainFrame;

    public MainMenuPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        JLabel titleLabel = new JLabel("出血性脑卒中 智能诊疗系统 主菜单");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 26));

        JButton patientManagementButton = new JButton("患者信息管理");
        JButton predictionButton = new JButton("血肿扩张预警");
        JButton logoutButton = new JButton("退出登录");

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(titleLabel, gbc);

        gbc.gridy++;
        add(patientManagementButton, gbc);

        gbc.gridy++;
        add(predictionButton, gbc);

        gbc.gridy++;
        add(logoutButton, gbc);

        // 按钮事件
        patientManagementButton.addActionListener(e -> mainFrame.showPatientManagementPanel());
        predictionButton.addActionListener(e -> mainFrame.showPredictionPanel());
        logoutButton.addActionListener(e -> mainFrame.showLoginPanel());
    }
}