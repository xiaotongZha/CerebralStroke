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

    // 创建按钮的通用方法
    private JButton createStyledButton(String text, Font buttonFont, Color buttonBg, Color buttonFg, Dimension buttonSize) {
        JButton button = new JButton(text);
        button.setFont(buttonFont);
        button.setBackground(buttonBg);
        button.setForeground(buttonFg);
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setPreferredSize(buttonSize);
        button.setMargin(new Insets(5, 20, 5, 20));
        
        // 添加鼠标悬停效果
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(30, 136, 229));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(buttonBg);
            }
        });
        
        return button;
    }

    private void initComponents() {
        // 设置面板背景颜色
        setBackground(new Color(245, 245, 245)); // 浅灰色背景
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); // 增加组件间距

        // 标题
        JLabel titleLabel = new JLabel("出血性脑卒中 智能诊疗系统 主菜单");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 26));
        titleLabel.setForeground(new Color(33, 150, 243)); // 蓝色字体
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;

        // 按钮样式
        Font buttonFont = new Font("微软雅黑", Font.BOLD, 16);
        Color buttonBg = new Color(33, 150, 243); // 蓝色背景
        Color buttonFg = Color.BLACK; // 白色字体
        Dimension buttonSize = new Dimension(300, 45); // 设置按钮大小

        // 患者信息管理按钮
        JButton patientManagementButton = createStyledButton("患者信息管理", buttonFont, buttonBg, buttonFg, buttonSize);

        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE; // 不填充，保持按钮原有大小
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(15, 20, 15, 20); // 调整按钮间距
        add(patientManagementButton, gbc);

        // 血肿扩张预警按钮
        JButton predictionButton = createStyledButton("血肿扩张预警", buttonFont, buttonBg, buttonFg, buttonSize);

        gbc.gridy++;
        add(predictionButton, gbc);

        // 退出登录按钮
        JButton logoutButton = createStyledButton("退出登录", buttonFont, buttonBg, buttonFg, buttonSize);

        gbc.gridy++;
        add(logoutButton, gbc);

        // 按钮事件
        patientManagementButton.addActionListener(e -> mainFrame.showPatientManagementPanel());
        predictionButton.addActionListener(e -> mainFrame.showPredictionPanel());
        logoutButton.addActionListener(e -> mainFrame.showLoginPanel());
    }
}