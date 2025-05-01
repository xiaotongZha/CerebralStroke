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
        this.predictService=new PredictService();
        this.mainFrame = mainFrame;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // 标题
        JLabel titleLabel = new JLabel("血肿扩张预警界面", JLabel.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        // 中部内容面板
        JPanel centerPanel = new JPanel(new BorderLayout());

        // 顶部按钮面板

        JButton computeTrainLabelsButton = new JButton("计算真实标签");
        computeTrainLabelsButton.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        computeTrainLabelsButton.addActionListener(e -> computeTrainLabels());

        JButton predictButton = new JButton("一键预警");
        predictButton.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        predictButton.addActionListener(e -> doPrediction());

        JPanel topButtonPanel = new JPanel();
        topButtonPanel.add(computeTrainLabelsButton);
        topButtonPanel.add(predictButton);
        centerPanel.add(topButtonPanel, BorderLayout.NORTH);

        // 图片展示面板
        imagePanel1 = new RatioImagePanel(4.0 / 3);
        imagePanel2 = new RatioImagePanel(4.0 / 3);

        JPanel imageContainer = new JPanel(new GridLayout(1, 2, 10, 10));
        imageContainer.add(imagePanel1);
        imageContainer.add(imagePanel2);
        centerPanel.add(imageContainer, BorderLayout.CENTER);

        trainLabelsTextArea = new JTextArea(2, 40);
        trainLabelsTextArea.setBorder(BorderFactory.createTitledBorder("训练集真实标签"));
        trainLabelsTextArea.setLineWrap(true);
        trainLabelsTextArea.setWrapStyleWord(true);

        // 文本信息面板
        probabilityTextArea = new JTextArea(2, 40);
        probabilityTextArea.setBorder(BorderFactory.createTitledBorder("发病概率报告"));
        probabilityTextArea.setLineWrap(true);
        probabilityTextArea.setWrapStyleWord(true);

        warningListTextArea = new JTextArea(2, 40);
        warningListTextArea.setBorder(BorderFactory.createTitledBorder("高风险患者名单"));
        warningListTextArea.setLineWrap(true);
        warningListTextArea.setWrapStyleWord(true);

        JPanel textPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        textPanel.add(new JScrollPane(trainLabelsTextArea));
        textPanel.add(new JScrollPane(probabilityTextArea));
        textPanel.add(new JScrollPane(warningListTextArea));
        centerPanel.add(textPanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        // 返回按钮
        JButton backButton = new JButton("返回主菜单");
        backButton.addActionListener(e -> mainFrame.showMainMenuPanel());
        add(backButton, BorderLayout.SOUTH);
    }

    private void doPrediction() {
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
        String trainLabels = predictService.getTrainLabels();
        trainLabelsTextArea.setText(trainLabels);
    }
}