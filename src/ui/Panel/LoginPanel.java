package ui.Panel;

import ui.MainFrame;
import service.LoginService;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    private MainFrame mainFrame;
    private LoginService loginService;

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.loginService = new LoginService();
        initComponents();
    }

    private void initComponents() {
        // 设置面板背景颜色
        setBackground(new Color(245, 245, 245)); // 浅灰色背景
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 30, 20, 30); // 增加组件间距

        // 标题
        JLabel titleLabel = new JLabel("医  生  登  录  系  统");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 150, 243)); // 蓝色字体
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;

        // 用户名
        JLabel userLabel = new JLabel("用户名:");
        userLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(userLabel, gbc);

        usernameField = new JTextField(15);
        usernameField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(usernameField, gbc);

        // 密码
        JLabel passLabel = new JLabel("密    码:");
        passLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        add(passLabel, gbc);

        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        add(passwordField, gbc);

        // 登录按钮
        loginButton = new JButton("登录");
        loginButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        loginButton.setBackground(new Color(33, 150, 243)); // 蓝色背景
        loginButton.setForeground(Color.BLACK); // 白色字体
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(true);
        loginButton.setPreferredSize(new Dimension(200, 40)); // 设置按钮大小
        loginButton.setMargin(new Insets(5, 20, 5, 20)); // 设置按钮内边距

        // 添加鼠标悬停效果
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(30, 136, 229)); // 深一点的蓝色
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(33, 150, 243)); // 恢复原来的蓝色
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE; // 不填充，保持按钮原有大小
        gbc.insets = new Insets(30, 30, 20, 30); // 增加按钮上下间距
        add(loginButton, gbc);

        // 登录按钮逻辑
        loginButton.addActionListener(e -> login());
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "用户名和密码不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = loginService.login(username, password);
        if (success) {
            JOptionPane.showMessageDialog(this, "登录成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
            mainFrame.showMainMenuPanel();
        } else {
            JOptionPane.showMessageDialog(this, "用户名或密码错误！", "登录失败", JOptionPane.ERROR_MESSAGE);
            // 可选：清空密码框
            passwordField.setText("");
        }
    }
}