package ui.Panel;

import model.Patient;
import ui.MainFrame;
import service.PatientService;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;
import utils.ParseUtils;

public class PatientManagementPanel extends JPanel {

    private MainFrame mainFrame;

    private JTextField idField, minAgeField, maxAgeField;
    private JComboBox<String> genderComboBox;

    private JTextField minMrsField, maxMrsField, minOnsetField, maxOnsetField;
    private JTextField minSystolicField, maxSystolicField, minDiastolicField, maxDiastolicField;

    private JComboBox<String> hypertensionComboBox, strokeComboBox, diabetesComboBox, atrialFibComboBox, coronaryComboBox;
    private JComboBox<String> smokingComboBox, drinkingComboBox, hemostaticComboBox, icpReductionComboBox, antihypertensiveComboBox;
    private JComboBox<String> sedationComboBox, antiemeticComboBox, trophicNerveComboBox;

    private JTable patientTable;
    private DefaultTableModel tableModel;
    private JButton searchButton, addButton, deleteButton, saveButton, backButton;

    private Set<String> modifiedPatientIds = new HashSet<>();

    private static final Map<String, String> EN_TO_CN = new LinkedHashMap<>();
    private static final Map<String, String> CN_TO_EN = new LinkedHashMap<>();

    static {
        EN_TO_CN.put("id", "患者ID");
        EN_TO_CN.put("age", "年龄");
        EN_TO_CN.put("gender", "性别");
        EN_TO_CN.put("mrsScore", "MRS评分");
        EN_TO_CN.put("hypertension", "高血压史");
        EN_TO_CN.put("stroke", "卒中史");
        EN_TO_CN.put("diabetes", "糖尿病史");
        EN_TO_CN.put("atrialFib", "房颤史");
        EN_TO_CN.put("coronary", "冠心病史");
        EN_TO_CN.put("smoking", "吸烟史");
        EN_TO_CN.put("drinking", "饮酒史");
        EN_TO_CN.put("onsetToImageHours", "发病到影像时间(h)");
        EN_TO_CN.put("systolicPressure", "收缩压");
        EN_TO_CN.put("diastolicPressure", "舒张压");
        EN_TO_CN.put("hemostatic", "止血治疗");
        EN_TO_CN.put("icpReduction", "颅内压降低治疗");
        EN_TO_CN.put("antihypertensive", "降压治疗");
        EN_TO_CN.put("sedation", "镇静镇痛");
        EN_TO_CN.put("antiemetic", "止吐胃保护");
        EN_TO_CN.put("trophicNerve", "神经营养");

        for (Map.Entry<String, String> entry : EN_TO_CN.entrySet()) {
            CN_TO_EN.put(entry.getValue(), entry.getKey());
        }
    }

    public PatientManagementPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        initSearchPanel();
        initTablePanel();
        initButtonPanel();
        // 初始化时加载所有患者数据
        searchPatients();
    }

    private void initSearchPanel() {
        JPanel searchOuterPanel = new JPanel();
        searchOuterPanel.setLayout(new BoxLayout(searchOuterPanel, BoxLayout.Y_AXIS));
        searchOuterPanel.setBorder(new TitledBorder("查询条件"));

        // 第一部分
        JPanel searchInnerPanel1 = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        searchInnerPanel1.add(new JLabel("患者ID:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        idField = new JTextField(10);
        searchInnerPanel1.add(idField, gbc);

        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.EAST;
        searchInnerPanel1.add(new JLabel("性别:"), gbc);

        gbc.gridx = 3;
        gbc.anchor = GridBagConstraints.WEST;
        genderComboBox = new JComboBox<>(new String[]{"全部", "男", "女"});
        searchInnerPanel1.add(genderComboBox, gbc);

        gbc.gridx = 4;
        gbc.anchor = GridBagConstraints.EAST;
        searchInnerPanel1.add(new JLabel("年龄:"), gbc);

        gbc.gridx = 5;
        gbc.anchor = GridBagConstraints.WEST;
        minAgeField = new JTextField(3);
        searchInnerPanel1.add(minAgeField, gbc);

        gbc.gridx = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        searchInnerPanel1.add(new JLabel("-"), gbc);

        gbc.gridx = 7;
        gbc.anchor = GridBagConstraints.WEST;
        maxAgeField = new JTextField(3);
        searchInnerPanel1.add(maxAgeField, gbc);

        // 第二部分
        JPanel searchInnerPanel2 = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        searchInnerPanel2.add(new JLabel("MRS评分:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        minMrsField = new JTextField(3);
        searchInnerPanel2.add(minMrsField, gbc);

        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        searchInnerPanel2.add(new JLabel("-"), gbc);

        gbc.gridx = 3;
        gbc.anchor = GridBagConstraints.WEST;
        maxMrsField = new JTextField(3);
        searchInnerPanel2.add(maxMrsField, gbc);

        gbc.gridx = 4;
        gbc.anchor = GridBagConstraints.EAST;
        searchInnerPanel2.add(new JLabel("发病到影像(h):"), gbc);

        gbc.gridx = 5;
        gbc.anchor = GridBagConstraints.WEST;
        minOnsetField = new JTextField(3);
        searchInnerPanel2.add(minOnsetField, gbc);

        gbc.gridx = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        searchInnerPanel2.add(new JLabel("-"), gbc);

        gbc.gridx = 7;
        gbc.anchor = GridBagConstraints.WEST;
        maxOnsetField = new JTextField(3);
        searchInnerPanel2.add(maxOnsetField, gbc);

        // 第三部分
        JPanel searchInnerPanel3 = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        searchInnerPanel3.add(new JLabel("收缩压:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        minSystolicField = new JTextField(3);
        searchInnerPanel3.add(minSystolicField, gbc);

        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        searchInnerPanel3.add(new JLabel("-"), gbc);

        gbc.gridx = 3;
        gbc.anchor = GridBagConstraints.WEST;
        maxSystolicField = new JTextField(3);
        searchInnerPanel3.add(maxSystolicField, gbc);

        gbc.gridx = 4;
        gbc.anchor = GridBagConstraints.EAST;
        searchInnerPanel3.add(new JLabel("舒张压:"), gbc);

        gbc.gridx = 5;
        gbc.anchor = GridBagConstraints.WEST;
        minDiastolicField = new JTextField(3);
        searchInnerPanel3.add(minDiastolicField, gbc);

        gbc.gridx = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        searchInnerPanel3.add(new JLabel("-"), gbc);

        gbc.gridx = 7;
        gbc.anchor = GridBagConstraints.WEST;
        maxDiastolicField = new JTextField(3);
        searchInnerPanel3.add(maxDiastolicField, gbc);

        // 第四五六部分
        JPanel searchInnerPanel4 = new JPanel(new GridBagLayout());
        JPanel searchInnerPanel5 = new JPanel(new GridBagLayout());
        JPanel searchInnerPanel6 = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        String[] options = {"不限", "有", "无"};
        // 初始化 ComboBox
        hypertensionComboBox = new JComboBox<>(options);
        strokeComboBox = new JComboBox<>(options);
        diabetesComboBox = new JComboBox<>(options);
        atrialFibComboBox = new JComboBox<>(options);
        coronaryComboBox = new JComboBox<>(options);
        smokingComboBox = new JComboBox<>(options);
        drinkingComboBox = new JComboBox<>(options);
        hemostaticComboBox = new JComboBox<>(options);
        icpReductionComboBox = new JComboBox<>(options);
        antihypertensiveComboBox = new JComboBox<>(options);
        sedationComboBox = new JComboBox<>(options);
        antiemeticComboBox = new JComboBox<>(options);
        trophicNerveComboBox = new JComboBox<>(options);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        searchInnerPanel4.add(new JLabel("高血压史"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        searchInnerPanel4.add(hypertensionComboBox, gbc);

        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.EAST;
        searchInnerPanel4.add(new JLabel("卒中史"), gbc);

        gbc.gridx = 3;
        gbc.anchor = GridBagConstraints.WEST;
        searchInnerPanel4.add(strokeComboBox, gbc);

        gbc.gridx = 4;
        gbc.anchor = GridBagConstraints.EAST;
        searchInnerPanel4.add(new JLabel("糖尿病史"), gbc);

        gbc.gridx = 5;
        gbc.anchor = GridBagConstraints.WEST;
        searchInnerPanel4.add(diabetesComboBox, gbc);

        gbc.gridx = 6;
        gbc.anchor = GridBagConstraints.EAST;
        searchInnerPanel4.add(new JLabel("房颤史"), gbc);

        gbc.gridx = 7;
        gbc.anchor = GridBagConstraints.WEST;
        searchInnerPanel4.add(atrialFibComboBox, gbc);

        gbc.gridx = 8;
        gbc.anchor = GridBagConstraints.EAST;
        searchInnerPanel4.add(new JLabel("冠心病史"), gbc);

        gbc.gridx = 9;
        gbc.anchor = GridBagConstraints.WEST;
        searchInnerPanel4.add(coronaryComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        searchInnerPanel5.add(new JLabel("吸烟史"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        searchInnerPanel5.add(smokingComboBox, gbc);

        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.EAST;
        searchInnerPanel5.add(new JLabel("饮酒史"), gbc);

        gbc.gridx = 3;
        gbc.anchor = GridBagConstraints.WEST;
        searchInnerPanel5.add(drinkingComboBox, gbc);

        gbc.gridx = 4;
        gbc.anchor = GridBagConstraints.EAST;
        searchInnerPanel5.add(new JLabel("止血治疗"), gbc);

        gbc.gridx = 5;
        gbc.anchor = GridBagConstraints.WEST;
        searchInnerPanel5.add(hemostaticComboBox, gbc);

        gbc.gridx = 6;
        gbc.anchor = GridBagConstraints.EAST;
        searchInnerPanel5.add(new JLabel("颅压降低治疗"), gbc);

        gbc.gridx = 7;
        gbc.anchor = GridBagConstraints.WEST;
        searchInnerPanel5.add(icpReductionComboBox, gbc);

        gbc.gridx = 8;
        gbc.anchor = GridBagConstraints.EAST;
        searchInnerPanel5.add(new JLabel("降压治疗"), gbc);

        gbc.gridx = 9;
        gbc.anchor = GridBagConstraints.WEST;
        searchInnerPanel5.add(antihypertensiveComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        searchInnerPanel6.add(new JLabel("镇静镇痛"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        searchInnerPanel6.add(sedationComboBox, gbc);

        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.EAST;
        searchInnerPanel6.add(new JLabel("止吐胃保护"), gbc);

        gbc.gridx = 3;
        gbc.anchor = GridBagConstraints.WEST;
        searchInnerPanel6.add(antiemeticComboBox, gbc);

        gbc.gridx = 4;
        gbc.anchor = GridBagConstraints.EAST;
        searchInnerPanel6.add(new JLabel("神经营养"), gbc);

        gbc.gridx = 5;
        gbc.anchor = GridBagConstraints.WEST;
        searchInnerPanel6.add(trophicNerveComboBox, gbc);

        // 查询按钮单独放底下
        JPanel searchButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchButtonPanel.setOpaque(false);
        searchButton = createSmallButton("查询");
        searchButton.addActionListener(e -> searchPatients());
        searchButtonPanel.add(searchButton);

        // 总包裹
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
        innerPanel.setOpaque(false);

        innerPanel.add(searchInnerPanel1);
        innerPanel.add(searchInnerPanel2);
        innerPanel.add(searchInnerPanel3);
        innerPanel.add(searchInnerPanel4);
        innerPanel.add(searchInnerPanel5);
        innerPanel.add(searchInnerPanel6);
        innerPanel.add(searchButtonPanel); // 查询按钮最后加上去

        searchOuterPanel.add(innerPanel, BorderLayout.CENTER);
        this.add(searchOuterPanel, BorderLayout.NORTH);
    }

    private void initTablePanel() {
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(new TitledBorder("查询结果"));

        String[] columnNames = EN_TO_CN.values().toArray(new String[0]);
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };

        patientTable = new JTable(tableModel);
        patientTable.getTableHeader().setReorderingAllowed(false);

        tableModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            if (row >= 0) {
                String id = ParseUtils.parseString(tableModel.getValueAt(row, 0));
                modifiedPatientIds.add(id);
            }
        });

        JScrollPane scrollPane = new JScrollPane(patientTable);
        resultPanel.add(scrollPane, BorderLayout.CENTER);

        add(resultPanel, BorderLayout.CENTER);
    }

    private void initButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        addButton = createSmallButton("新增患者");
        addButton.addActionListener(e -> showAddDialog());
        buttonPanel.add(addButton);

        JButton editButton = createSmallButton("修改患者");
        editButton.addActionListener(e -> showEditDialog());
        buttonPanel.add(editButton);

        deleteButton = createSmallButton("删除患者");
        deleteButton.addActionListener(e -> deletePatient());
        buttonPanel.add(deleteButton);

        backButton = createSmallButton("返回菜单");
        backButton.addActionListener(e -> backToMenu());
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void showAddDialog() {
        JDialog addPatientDialog = new JDialog();
        addPatientDialog.setTitle("新增患者");

        // 创建主面板，使用GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 创建基本信息面板
        JPanel basicInfoPanel = new JPanel(new GridBagLayout());
        basicInfoPanel.setBorder(BorderFactory.createTitledBorder("基本信息"));
        GridBagConstraints basicGbc = new GridBagConstraints();
        basicGbc.insets = new Insets(5, 5, 5, 5);
        basicGbc.fill = GridBagConstraints.HORIZONTAL;

        // 基本信息组件
        JTextField idField = new JTextField(10);
        JTextField ageField = new JTextField(10);
        JComboBox<String> genderComboBox = new JComboBox<>(new String[]{"男", "女"});
        JTextField mrsField = new JTextField(10);
        JTextField onsetField = new JTextField(10);
        JTextField systolicField = new JTextField(10);
        JTextField diastolicField = new JTextField(10);

        // 添加基本信息组件
        int basicRow = 0;
        addComponent(basicInfoPanel, new JLabel("患者ID:"), basicGbc, 0, basicRow);
        addComponent(basicInfoPanel, idField, basicGbc, 1, basicRow++);
        addComponent(basicInfoPanel, new JLabel("年龄:"), basicGbc, 0, basicRow);
        addComponent(basicInfoPanel, ageField, basicGbc, 1, basicRow++);
        addComponent(basicInfoPanel, new JLabel("性别:"), basicGbc, 0, basicRow);
        addComponent(basicInfoPanel, genderComboBox, basicGbc, 1, basicRow++);
        addComponent(basicInfoPanel, new JLabel("MRS评分:"), basicGbc, 0, basicRow);
        addComponent(basicInfoPanel, mrsField, basicGbc, 1, basicRow++);
        addComponent(basicInfoPanel, new JLabel("发病到影像时间(h):"), basicGbc, 0, basicRow);
        addComponent(basicInfoPanel, onsetField, basicGbc, 1, basicRow++);
        addComponent(basicInfoPanel, new JLabel("收缩压:"), basicGbc, 0, basicRow);
        addComponent(basicInfoPanel, systolicField, basicGbc, 1, basicRow++);
        addComponent(basicInfoPanel, new JLabel("舒张压:"), basicGbc, 0, basicRow);
        addComponent(basicInfoPanel, diastolicField, basicGbc, 1, basicRow++);

        // 创建病史信息面板
        JPanel historyPanel = new JPanel(new GridBagLayout());
        historyPanel.setBorder(BorderFactory.createTitledBorder("病史信息"));
        GridBagConstraints historyGbc = new GridBagConstraints();
        historyGbc.insets = new Insets(5, 5, 5, 5);
        historyGbc.fill = GridBagConstraints.HORIZONTAL;

        // 病史复选框
        JCheckBox hypertensionCheckBox = new JCheckBox("高血压史");
        JCheckBox strokeCheckBox = new JCheckBox("卒中史");
        JCheckBox diabetesCheckBox = new JCheckBox("糖尿病史");
        JCheckBox atrialFibCheckBox = new JCheckBox("房颤史");
        JCheckBox coronaryCheckBox = new JCheckBox("冠心病史");
        JCheckBox smokingCheckBox = new JCheckBox("吸烟史");
        JCheckBox drinkingCheckBox = new JCheckBox("饮酒史");

        // 添加病史复选框
        int historyRow = 0;
        int historyCol = 0;
        addComponent(historyPanel, hypertensionCheckBox, historyGbc, historyCol++, historyRow);
        addComponent(historyPanel, strokeCheckBox, historyGbc, historyCol++, historyRow);
        addComponent(historyPanel, diabetesCheckBox, historyGbc, historyCol++, historyRow);
        historyCol = 0;
        historyRow++;
        addComponent(historyPanel, atrialFibCheckBox, historyGbc, historyCol++, historyRow);
        addComponent(historyPanel, coronaryCheckBox, historyGbc, historyCol++, historyRow);
        addComponent(historyPanel, smokingCheckBox, historyGbc, historyCol++, historyRow);
        addComponent(historyPanel, drinkingCheckBox, historyGbc, historyCol++, historyRow);

        // 创建治疗措施面板
        JPanel treatmentPanel = new JPanel(new GridBagLayout());
        treatmentPanel.setBorder(BorderFactory.createTitledBorder("治疗措施"));
        GridBagConstraints treatmentGbc = new GridBagConstraints();
        treatmentGbc.insets = new Insets(5, 5, 5, 5);
        treatmentGbc.fill = GridBagConstraints.HORIZONTAL;

        // 治疗措施复选框
        JCheckBox hemostaticCheckBox = new JCheckBox("止血治疗");
        JCheckBox icpReductionCheckBox = new JCheckBox("颅内压降低治疗");
        JCheckBox antihypertensiveCheckBox = new JCheckBox("降压治疗");
        JCheckBox sedationCheckBox = new JCheckBox("镇静镇痛");
        JCheckBox antiemeticCheckBox = new JCheckBox("止吐胃保护");
        JCheckBox trophicNerveCheckBox = new JCheckBox("神经营养");

        // 添加治疗措施复选框
        int treatmentRow = 0;
        int treatmentCol = 0;
        addComponent(treatmentPanel, hemostaticCheckBox, treatmentGbc, treatmentCol++, treatmentRow);
        addComponent(treatmentPanel, icpReductionCheckBox, treatmentGbc, treatmentCol++, treatmentRow);
        addComponent(treatmentPanel, antihypertensiveCheckBox, treatmentGbc, treatmentCol++, treatmentRow);
        treatmentCol = 0;
        treatmentRow++;
        addComponent(treatmentPanel, sedationCheckBox, treatmentGbc, treatmentCol++, treatmentRow);
        addComponent(treatmentPanel, antiemeticCheckBox, treatmentGbc, treatmentCol++, treatmentRow);
        addComponent(treatmentPanel, trophicNerveCheckBox, treatmentGbc, treatmentCol++, treatmentRow);

        // 创建按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveButton = createSmallButton("保存");
        saveButton.addActionListener(e -> {
            try {
                Patient newPatient = new Patient();
                newPatient.setId(idField.getText());
                newPatient.setAge(Integer.parseInt(ageField.getText()));
                String gender = genderComboBox.getSelectedItem().equals("男") ? "M" : "F";
                newPatient.setGender(gender);
                newPatient.setMrsScore(Integer.parseInt(mrsField.getText()));
                newPatient.setOnsetToImagingHours(Double.parseDouble(onsetField.getText()));
                newPatient.setSystolicPressure(Integer.parseInt(systolicField.getText()));
                newPatient.setDiastolicPressure(Integer.parseInt(diastolicField.getText()));
                newPatient.setHypertensionHistory(hypertensionCheckBox.isSelected() ? 1 : 0);
                newPatient.setStrokeHistory(strokeCheckBox.isSelected() ? 1 : 0);
                newPatient.setDiabetesHistory(diabetesCheckBox.isSelected() ? 1 : 0);
                newPatient.setAfHistory(atrialFibCheckBox.isSelected() ? 1 : 0);
                newPatient.setChdHistory(coronaryCheckBox.isSelected() ? 1 : 0);
                newPatient.setSmokingHistory(smokingCheckBox.isSelected() ? 1 : 0);
                newPatient.setDrinkingHistory(drinkingCheckBox.isSelected() ? 1 : 0);

                newPatient.setHemostaticTherapy(hemostaticCheckBox.isSelected() ? 1 : 0);
                newPatient.setIcpReductionTherapy(icpReductionCheckBox.isSelected() ? 1 : 0);
                newPatient.setAntihypertensiveTherapy(antihypertensiveCheckBox.isSelected() ? 1 : 0);
                newPatient.setSedationAnalgesia(sedationCheckBox.isSelected() ? 1 : 0);
                newPatient.setAntiemeticGastricProtection(antiemeticCheckBox.isSelected() ? 1 : 0);
                newPatient.setNeurotrophicTherapy(trophicNerveCheckBox.isSelected() ? 1 : 0);

                // 保存患者信息
                mainFrame.getPatientService().addPatient(newPatient);
                JOptionPane.showMessageDialog(addPatientDialog, "患者信息添加成功！");
                addPatientDialog.dispose();
                searchPatients(); // 重新查询并刷新表格
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addPatientDialog, "请输入有效的数字！");
            }
        });
        buttonPanel.add(saveButton);

        // 将所有面板添加到主面板
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(basicInfoPanel, gbc);

        gbc.gridy = 1;
        mainPanel.add(historyPanel, gbc);

        gbc.gridy = 2;
        mainPanel.add(treatmentPanel, gbc);

        gbc.gridy = 3;
        mainPanel.add(buttonPanel, gbc);

        addPatientDialog.add(mainPanel);
        addPatientDialog.setSize(500, 600);
        addPatientDialog.setLocationRelativeTo(null);
        addPatientDialog.setVisible(true);
    }

    // 辅助方法：添加组件到面板
    private void addComponent(JPanel panel, JComponent component, GridBagConstraints gbc, int gridx, int gridy) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        panel.add(component, gbc);
    }

    private void showEditDialog() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择要修改的患者！");
            return;
        }

        // 获取当前选中患者ID
        String patientId = ParseUtils.parseString(tableModel.getValueAt(selectedRow, 0));
        Patient patient = mainFrame.getPatientService().getPatientById(patientId);
        if (patient == null) {
            JOptionPane.showMessageDialog(this, "患者信息不存在！");
            return;
        }

        JDialog editDialog = new JDialog();
        editDialog.setTitle("修改患者信息");

        // 创建主面板，使用GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 创建基本信息面板
        JPanel basicInfoPanel = new JPanel(new GridBagLayout());
        basicInfoPanel.setBorder(BorderFactory.createTitledBorder("基本信息"));
        GridBagConstraints basicGbc = new GridBagConstraints();
        basicGbc.insets = new Insets(5, 5, 5, 5);
        basicGbc.fill = GridBagConstraints.HORIZONTAL;

        // 基本信息组件
        JTextField idField = new JTextField(patient.getId());
        idField.setEditable(false);
        JTextField ageField = new JTextField(String.valueOf(patient.getAge()));
        JComboBox<String> genderComboBox = new JComboBox<>(new String[]{"男", "女"});
        genderComboBox.setSelectedItem(patient.getGender().equals("M") ? "男" : "女");
        JTextField mrsField = new JTextField(String.valueOf(patient.getMrsScore()));
        JTextField onsetField = new JTextField(String.valueOf(patient.getOnsetToImagingHours()));
        JTextField systolicField = new JTextField(String.valueOf(patient.getSystolicPressure()));
        JTextField diastolicField = new JTextField(String.valueOf(patient.getDiastolicPressure()));

        // 添加基本信息组件
        int basicRow = 0;
        addComponent(basicInfoPanel, new JLabel("患者ID:"), basicGbc, 0, basicRow);
        addComponent(basicInfoPanel, idField, basicGbc, 1, basicRow++);
        addComponent(basicInfoPanel, new JLabel("年龄:"), basicGbc, 0, basicRow);
        addComponent(basicInfoPanel, ageField, basicGbc, 1, basicRow++);
        addComponent(basicInfoPanel, new JLabel("性别:"), basicGbc, 0, basicRow);
        addComponent(basicInfoPanel, genderComboBox, basicGbc, 1, basicRow++);
        addComponent(basicInfoPanel, new JLabel("MRS评分:"), basicGbc, 0, basicRow);
        addComponent(basicInfoPanel, mrsField, basicGbc, 1, basicRow++);
        addComponent(basicInfoPanel, new JLabel("发病到影像时间(h):"), basicGbc, 0, basicRow);
        addComponent(basicInfoPanel, onsetField, basicGbc, 1, basicRow++);
        addComponent(basicInfoPanel, new JLabel("收缩压:"), basicGbc, 0, basicRow);
        addComponent(basicInfoPanel, systolicField, basicGbc, 1, basicRow++);
        addComponent(basicInfoPanel, new JLabel("舒张压:"), basicGbc, 0, basicRow);
        addComponent(basicInfoPanel, diastolicField, basicGbc, 1, basicRow++);

        // 创建病史信息面板
        JPanel historyPanel = new JPanel(new GridBagLayout());
        historyPanel.setBorder(BorderFactory.createTitledBorder("病史信息"));
        GridBagConstraints historyGbc = new GridBagConstraints();
        historyGbc.insets = new Insets(5, 5, 5, 5);
        historyGbc.fill = GridBagConstraints.HORIZONTAL;

        // 病史复选框
        JCheckBox hypertensionCheckBox = new JCheckBox("高血压史");
        hypertensionCheckBox.setSelected(patient.getHypertensionHistory() == 1);
        JCheckBox strokeCheckBox = new JCheckBox("卒中史");
        strokeCheckBox.setSelected(patient.getStrokeHistory() == 1);
        JCheckBox diabetesCheckBox = new JCheckBox("糖尿病史");
        diabetesCheckBox.setSelected(patient.getDiabetesHistory() == 1);
        JCheckBox atrialFibCheckBox = new JCheckBox("房颤史");
        atrialFibCheckBox.setSelected(patient.getAfHistory() == 1);
        JCheckBox coronaryCheckBox = new JCheckBox("冠心病史");
        coronaryCheckBox.setSelected(patient.getChdHistory() == 1);
        JCheckBox smokingCheckBox = new JCheckBox("吸烟史");
        smokingCheckBox.setSelected(patient.getSmokingHistory() == 1);
        JCheckBox drinkingCheckBox = new JCheckBox("饮酒史");
        drinkingCheckBox.setSelected(patient.getDrinkingHistory() == 1);

        // 添加病史复选框
        int historyRow = 0;
        int historyCol = 0;
        addComponent(historyPanel, hypertensionCheckBox, historyGbc, historyCol++, historyRow);
        addComponent(historyPanel, strokeCheckBox, historyGbc, historyCol++, historyRow);
        addComponent(historyPanel, diabetesCheckBox, historyGbc, historyCol++, historyRow);
        historyCol = 0;
        historyRow++;
        addComponent(historyPanel, atrialFibCheckBox, historyGbc, historyCol++, historyRow);
        addComponent(historyPanel, coronaryCheckBox, historyGbc, historyCol++, historyRow);
        addComponent(historyPanel, smokingCheckBox, historyGbc, historyCol++, historyRow);
        addComponent(historyPanel, drinkingCheckBox, historyGbc, historyCol++, historyRow);

        // 创建治疗措施面板
        JPanel treatmentPanel = new JPanel(new GridBagLayout());
        treatmentPanel.setBorder(BorderFactory.createTitledBorder("治疗措施"));
        GridBagConstraints treatmentGbc = new GridBagConstraints();
        treatmentGbc.insets = new Insets(5, 5, 5, 5);
        treatmentGbc.fill = GridBagConstraints.HORIZONTAL;

        // 治疗措施复选框
        JCheckBox hemostaticCheckBox = new JCheckBox("止血治疗");
        hemostaticCheckBox.setSelected(patient.getHemostaticTherapy() == 1);
        JCheckBox icpReductionCheckBox = new JCheckBox("颅内压降低治疗");
        icpReductionCheckBox.setSelected(patient.getIcpReductionTherapy() == 1);
        JCheckBox antihypertensiveCheckBox = new JCheckBox("降压治疗");
        antihypertensiveCheckBox.setSelected(patient.getAntihypertensiveTherapy() == 1);
        JCheckBox sedationCheckBox = new JCheckBox("镇静镇痛");
        sedationCheckBox.setSelected(patient.getSedationAnalgesia() == 1);
        JCheckBox antiemeticCheckBox = new JCheckBox("止吐胃保护");
        antiemeticCheckBox.setSelected(patient.getAntiemeticGastricProtection() == 1);
        JCheckBox trophicNerveCheckBox = new JCheckBox("神经营养");
        trophicNerveCheckBox.setSelected(patient.getNeurotrophicTherapy() == 1);

        // 添加治疗措施复选框
        int treatmentRow = 0;
        int treatmentCol = 0;
        addComponent(treatmentPanel, hemostaticCheckBox, treatmentGbc, treatmentCol++, treatmentRow);
        addComponent(treatmentPanel, icpReductionCheckBox, treatmentGbc, treatmentCol++, treatmentRow);
        addComponent(treatmentPanel, antihypertensiveCheckBox, treatmentGbc, treatmentCol++, treatmentRow);
        treatmentCol = 0;
        treatmentRow++;
        addComponent(treatmentPanel, sedationCheckBox, treatmentGbc, treatmentCol++, treatmentRow);
        addComponent(treatmentPanel, antiemeticCheckBox, treatmentGbc, treatmentCol++, treatmentRow);
        addComponent(treatmentPanel, trophicNerveCheckBox, treatmentGbc, treatmentCol++, treatmentRow);

        // 创建按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveButton = createSmallButton("保存修改");
        saveButton.addActionListener(e -> {
            try {
                // 更新患者对象
                patient.setAge(Integer.parseInt(ageField.getText()));
                patient.setGender(genderComboBox.getSelectedItem().equals("男") ? "M" : "F");
                patient.setMrsScore(Integer.parseInt(mrsField.getText()));
                patient.setOnsetToImagingHours(Double.parseDouble(onsetField.getText()));
                patient.setSystolicPressure(Integer.parseInt(systolicField.getText()));
                patient.setDiastolicPressure(Integer.parseInt(diastolicField.getText()));

                // 更新病史信息
                patient.setHypertensionHistory(hypertensionCheckBox.isSelected() ? 1 : 0);
                patient.setStrokeHistory(strokeCheckBox.isSelected() ? 1 : 0);
                patient.setDiabetesHistory(diabetesCheckBox.isSelected() ? 1 : 0);
                patient.setAfHistory(atrialFibCheckBox.isSelected() ? 1 : 0);
                patient.setChdHistory(coronaryCheckBox.isSelected() ? 1 : 0);
                patient.setSmokingHistory(smokingCheckBox.isSelected() ? 1 : 0);
                patient.setDrinkingHistory(drinkingCheckBox.isSelected() ? 1 : 0);

                // 更新治疗措施
                patient.setHemostaticTherapy(hemostaticCheckBox.isSelected() ? 1 : 0);
                patient.setIcpReductionTherapy(icpReductionCheckBox.isSelected() ? 1 : 0);
                patient.setAntihypertensiveTherapy(antihypertensiveCheckBox.isSelected() ? 1 : 0);
                patient.setSedationAnalgesia(sedationCheckBox.isSelected() ? 1 : 0);
                patient.setAntiemeticGastricProtection(antiemeticCheckBox.isSelected() ? 1 : 0);
                patient.setNeurotrophicTherapy(trophicNerveCheckBox.isSelected() ? 1 : 0);

                // 保存修改
                mainFrame.getPatientService().updatePatient(patient);
                JOptionPane.showMessageDialog(editDialog, "修改成功！");
                editDialog.dispose();
                searchPatients(); // 刷新表格
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(editDialog, "请输入有效的数字！");
            }
        });
        buttonPanel.add(saveButton);

        // 将所有面板添加到主面板
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(basicInfoPanel, gbc);

        gbc.gridy = 1;
        mainPanel.add(historyPanel, gbc);

        gbc.gridy = 2;
        mainPanel.add(treatmentPanel, gbc);

        gbc.gridy = 3;
        mainPanel.add(buttonPanel, gbc);

        editDialog.add(mainPanel);
        editDialog.setSize(500, 600);
        editDialog.setLocationRelativeTo(null);
        editDialog.setModal(true);
        editDialog.setVisible(true);
    }

    private void searchPatients() {
        // 创建条件字典
        Map<String, Object> searchCriteria = new HashMap<>();

        // 添加基本信息到字典
        searchCriteria.put("id", ParseUtils.parseString(idField.getText().trim()));
        searchCriteria.put("gender", parseGender(genderComboBox));
        searchCriteria.put("minAge", ParseUtils.parseInteger(minAgeField.getText().trim()));
        searchCriteria.put("maxAge", ParseUtils.parseInteger(maxAgeField.getText().trim()));
        searchCriteria.put("minMrs", ParseUtils.parseInteger(minMrsField.getText().trim()));
        searchCriteria.put("maxMrs", ParseUtils.parseInteger(maxMrsField.getText().trim()));
        searchCriteria.put("minOnset", ParseUtils.parseDouble(minOnsetField.getText().trim()));
        searchCriteria.put("maxOnset", ParseUtils.parseDouble(maxOnsetField.getText().trim()));
        searchCriteria.put("minSystolic", ParseUtils.parseInteger(minSystolicField.getText().trim()));
        searchCriteria.put("maxSystolic", ParseUtils.parseInteger(maxSystolicField.getText().trim()));
        searchCriteria.put("minDiastolic", ParseUtils.parseInteger(minDiastolicField.getText().trim()));
        searchCriteria.put("maxDiastolic", ParseUtils.parseInteger(maxDiastolicField.getText().trim()));

        // 添加病史条件到字典
        searchCriteria.put("hypertension", getHistoryCondition(hypertensionComboBox));
        searchCriteria.put("stroke", getHistoryCondition(strokeComboBox));
        searchCriteria.put("diabetes", getHistoryCondition(diabetesComboBox));
        searchCriteria.put("atrialFib", getHistoryCondition(atrialFibComboBox));
        searchCriteria.put("coronary", getHistoryCondition(coronaryComboBox));
        searchCriteria.put("smoking", getHistoryCondition(smokingComboBox));
        searchCriteria.put("drinking", getHistoryCondition(drinkingComboBox));
        searchCriteria.put("hemostatic", getHistoryCondition(hemostaticComboBox));
        searchCriteria.put("icpReduction", getHistoryCondition(icpReductionComboBox));
        searchCriteria.put("antihypertensive", getHistoryCondition(antihypertensiveComboBox));
        searchCriteria.put("sedation", getHistoryCondition(sedationComboBox));
        searchCriteria.put("antiemetic", getHistoryCondition(antiemeticComboBox));
        searchCriteria.put("trophicNerve", getHistoryCondition(trophicNerveComboBox));

        // 调用查询方法
        List<Patient> result = mainFrame.getPatientService().searchPatients(searchCriteria);

        modifiedPatientIds.clear();
        String[] columnNames = EN_TO_CN.values().toArray(new String[0]);
        DefaultTableModel newModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        newModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            if (row >= 0) {
                String id = ParseUtils.parseString(newModel.getValueAt(row, 0));
                modifiedPatientIds.add(id);
            }
        });

        // 添加查询结果到表格
        for (Patient p : result) {
            newModel.addRow(new Object[]{
                    p.getId(), p.getAge(), p.getGender(), p.getMrsScore(),
                    p.getHypertensionHistory(), p.getStrokeHistory(), p.getDiabetesHistory(),
                    p.getAfHistory(), p.getChdHistory(), p.getSmokingHistory(), p.getDrinkingHistory(),
                    p.getOnsetToImagingHours(), p.getSystolicPressure(), p.getDiastolicPressure(),
                    p.getHemostaticTherapy(), p.getIcpReductionTherapy(), p.getAntihypertensiveTherapy(),
                    p.getSedationAnalgesia(), p.getAntiemeticGastricProtection(), p.getNeurotrophicTherapy()
            });
        }
        patientTable.setModel(newModel);
        tableModel = newModel;
    }

    private Integer getHistoryCondition(JComboBox<String> comboBox) {
        String selected = (String) comboBox.getSelectedItem();
        if ("不限".equals(selected)) return null;
        return "有".equals(selected) ? 1 : 0;
    }

    private String parseGender(JComboBox<String> comboBox) {
        String selected = (String) comboBox.getSelectedItem();
        if ("全部".equals(selected)) return null;
        return "男".equals(selected) ? "M" : "F";
    }

    private void deletePatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选中一行！");
            return;
        }

        String id = ParseUtils.parseString(tableModel.getValueAt(selectedRow, 0));
        int confirm = JOptionPane.showConfirmDialog(this, "确认删除ID为 " + id + " 的患者？", "确认删除", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            mainFrame.getPatientService().deletePatient(id);
            searchPatients();
            JOptionPane.showMessageDialog(this, "删除成功！");
        }
    }

    private void backToMenu() {
        mainFrame.showMainMenuPanel();
    }

    private JButton createSmallButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        button.setBackground(new Color(33, 150, 243));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setPreferredSize(new Dimension(100, 30));
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
}