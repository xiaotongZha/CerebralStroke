package ui;

import ui.Panel.LoginPanel;
import ui.Panel.MainMenuPanel;
import ui.Panel.PatientManagementPanel;
import ui.Panel.PredictionPanel;
import service.PatientService;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private PatientService patientService;
    public PatientService getPatientService() {
        return patientService;
    }

    public static final String LOGIN_PANEL = "LoginPanel";
    public static final String MAIN_MENU_PANEL = "MainMenuPanel";
    public static final String PATIENT_MANAGEMENT_PANEL = "PatientManagementPanel";
    public static final String PREDICTION_PANEL = "PredictionPanel";

    public MainFrame() {

        patientService=new PatientService();
        // 设置全局字体防止中文乱码
        setUIFont(new javax.swing.plaf.FontUIResource("微软雅黑", Font.PLAIN, 14));
        setTitle("出血性脑卒中智能诊疗系统");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // 初始化各个界面
        LoginPanel loginPanel = new LoginPanel(this);
        MainMenuPanel mainMenuPanel = new MainMenuPanel(this);
        PatientManagementPanel patientManagementPanel = new PatientManagementPanel(this);
        PredictionPanel predictionPanel = new PredictionPanel(this);

        cardPanel.add(loginPanel, LOGIN_PANEL);
        cardPanel.add(mainMenuPanel, MAIN_MENU_PANEL);
        cardPanel.add(patientManagementPanel, PATIENT_MANAGEMENT_PANEL);
        cardPanel.add(predictionPanel, PREDICTION_PANEL);

        add(cardPanel);
        showLoginPanel();
    }

    public void showLoginPanel() {
        cardLayout.show(cardPanel, LOGIN_PANEL);
    }

    public void showMainMenuPanel() {
        cardLayout.show(cardPanel, MAIN_MENU_PANEL);
    }

    public void showPatientManagementPanel() {
        cardLayout.show(cardPanel, PATIENT_MANAGEMENT_PANEL);
    }

    public void showPredictionPanel() {
        cardLayout.show(cardPanel, PREDICTION_PANEL);
    }

    // 设置全局默认字体，防止中文乱码
    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put(key, f);
        }
    }
}
