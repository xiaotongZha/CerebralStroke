package ui.Panel;

import service.PredictService;
import ui.MainFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PredictionPanel extends JPanel {
    private MainFrame mainFrame;
    private RatioImagePanel imagePanel1;
    private RatioImagePanel imagePanel2;
    private JTextArea probabilityTextArea;
    private JTextArea warningListTextArea;
    private JTextArea trainLabelsTextArea;
    private PredictService predictService;

    public PredictionPanel(MainFrame mainFrame) {
        this.predictService = new PredictService();
        this.mainFrame = mainFrame;
        initComponents();
    }

    private JButton createSmallButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        button.setBackground(new Color(33, 150, 243));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setPreferredSize(new Dimension(120, 30));
        button.setMargin(new Insets(2, 10, 2, 10));
        
        // 添加鼠标悬停效果
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(30, 136, 229));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(33, 150, 243));
            }
        });
        
        return button;
    }

    private void initComponents() {
        setBackground(new Color(245, 245, 245));
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 标题
        JLabel titleLabel = new JLabel("血肿扩张预警界面", JLabel.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        titleLabel.setForeground(new Color(33, 150, 243));
        add(titleLabel, BorderLayout.NORTH);

        // 中部内容面板
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(new Color(245, 245, 245));

        // 顶部按钮面板
        JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        topButtonPanel.setBackground(new Color(245, 245, 245));

        JButton computeTrainLabelsButton = createSmallButton("计算真实标签");
        computeTrainLabelsButton.addActionListener(e -> computeTrainLabels());

        JButton predictButton = createSmallButton("一键预警");
        predictButton.addActionListener(e -> doPrediction());

        topButtonPanel.add(computeTrainLabelsButton);
        topButtonPanel.add(predictButton);
        centerPanel.add(topButtonPanel, BorderLayout.NORTH);

        // 图片展示面板
        JPanel imageContainer = new JPanel(new GridLayout(1, 2, 10, 10));
        imageContainer.setBackground(new Color(245, 245, 245));
        imagePanel1 = new RatioImagePanel(4.0 / 3);
        imagePanel2 = new RatioImagePanel(4.0 / 3);
        imageContainer.add(imagePanel1);
        imageContainer.add(imagePanel2);
        centerPanel.add(imageContainer, BorderLayout.CENTER);

        // 文本信息面板
        JPanel textPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        textPanel.setBackground(new Color(245, 245, 245));

        trainLabelsTextArea = new JTextArea(2, 40);
        trainLabelsTextArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        trainLabelsTextArea.setBorder(BorderFactory.createTitledBorder("训练集真实标签"));
        trainLabelsTextArea.setLineWrap(true);
        trainLabelsTextArea.setWrapStyleWord(true);

        probabilityTextArea = new JTextArea(2, 40);
        probabilityTextArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        probabilityTextArea.setBorder(BorderFactory.createTitledBorder("发病概率报告"));
        probabilityTextArea.setLineWrap(true);
        probabilityTextArea.setWrapStyleWord(true);

        warningListTextArea = new JTextArea(2, 40);
        warningListTextArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        warningListTextArea.setBorder(BorderFactory.createTitledBorder("高风险患者名单"));
        warningListTextArea.setLineWrap(true);
        warningListTextArea.setWrapStyleWord(true);

        textPanel.add(new JScrollPane(trainLabelsTextArea));
        textPanel.add(new JScrollPane(probabilityTextArea));
        textPanel.add(new JScrollPane(warningListTextArea));
        centerPanel.add(textPanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        // 底部按钮面板
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(245, 245, 245));
        JButton backButton = createSmallButton("返回主菜单");
        backButton.addActionListener(e -> mainFrame.showMainMenuPanel());
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void doPrediction() {
        predictService.runTrainEvalTest();
        imagePanel1.setImage(null);
        imagePanel2.setImage(null);
        probabilityTextArea.setText("");
        warningListTextArea.setText("");

        String imagePath1 = predictService.getMetricsPlotPath();
        String imagePath2 = predictService.getROCPlotPath();
        String probabilityReport = predictService.getProbabilityReport();
        String warningList = predictService.getWarningPatients();

        try {
            BufferedImage img1 = ImageIO.read(new File(imagePath1));
            BufferedImage img2 = ImageIO.read(new File(imagePath2));
            imagePanel1.setImage(img1);
            imagePanel2.setImage(img2);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        probabilityTextArea.setText(probabilityReport);
        warningListTextArea.setText(warningList);
    }

    private void computeTrainLabels() {
        predictService.runDataProcessor();
        String trainLabels = predictService.getTrainLabels();
        trainLabelsTextArea.setText(trainLabels);
    }
}