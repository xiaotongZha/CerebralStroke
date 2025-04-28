package ui.Panel;

import ui.MainFrame;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    private MainFrame mainFrame;

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("医生登录系统");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));

        JLabel userLabel = new JLabel("用户名:");
        JLabel passLabel = new JLabel("密码:");
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("登录");

        // 标题
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;

        // 用户名
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(userLabel, gbc);
        gbc.gridx = 1;
        add(usernameField, gbc);

        // 密码
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passLabel, gbc);
        gbc.gridx = 1;
        add(passwordField, gbc);

        // 登录按钮
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(loginButton, gbc);

        // 登录按钮逻辑
        loginButton.addActionListener(e -> login());
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // 简单模拟，真实场景应去调用 UserService 去数据库校验
        if ("doctor".equals(username) && "123456".equals(password)) {
            JOptionPane.showMessageDialog(this, "登录成功！");
            mainFrame.showMainMenuPanel();
        } else {
            JOptionPane.showMessageDialog(this, "用户名或密码错误！");
        }
    }
}
