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
    }


    private void initSearchPanel() {
        JPanel searchOuterPanel = new JPanel();
        searchOuterPanel.setLayout(new BoxLayout(searchOuterPanel,BoxLayout.Y_AXIS));
        searchOuterPanel.setBorder(new TitledBorder("查询条件"));

        //第一部分
        JPanel searchInnerPanel1= new JPanel(new FlowLayout(FlowLayout.LEFT));

        idField = new JTextField(6);
        searchInnerPanel1.add(new JLabel("患者ID:"));
        searchInnerPanel1.add(idField);
        searchInnerPanel1.add(Box.createHorizontalStrut(20));

        genderComboBox = new JComboBox<>(new String[]{"全部", "男", "女"});
        searchInnerPanel1.add(new JLabel("性别:"));
        searchInnerPanel1.add(genderComboBox);
        searchInnerPanel1.add(Box.createHorizontalStrut(20));

        minAgeField = new JTextField(3);
        maxAgeField = new JTextField(3);
        searchInnerPanel1.add(new JLabel("年龄:"));
        searchInnerPanel1.add(minAgeField);
        searchInnerPanel1.add(new JLabel("-"));
        searchInnerPanel1.add(maxAgeField);

        //第二部分
        JPanel searchInnerPanel2= new JPanel(new FlowLayout(FlowLayout.LEFT));

        minMrsField = new JTextField(3);
        maxMrsField = new JTextField(3);
        searchInnerPanel2.add(new JLabel("MRS评分:"));
        searchInnerPanel2.add(minMrsField);
        searchInnerPanel2.add(new JLabel("-"));
        searchInnerPanel2.add(maxMrsField);
        searchInnerPanel2.add(Box.createHorizontalStrut(20));

        minOnsetField = new JTextField(3);
        maxOnsetField = new JTextField(3);
        searchInnerPanel2.add(new JLabel("发病到影像(h):"));
        searchInnerPanel2.add(minOnsetField);
        searchInnerPanel2.add(new JLabel("-"));
        searchInnerPanel2.add(maxOnsetField);

        //第三部分
        JPanel searchInnerPanel3= new JPanel(new FlowLayout(FlowLayout.LEFT));
        minSystolicField = new JTextField(3);
        maxSystolicField = new JTextField(3);
        searchInnerPanel3.add(new JLabel("收缩压:"));
        searchInnerPanel3.add(minSystolicField);
        searchInnerPanel3.add(new JLabel("-"));
        searchInnerPanel3.add(maxSystolicField);
        searchInnerPanel3.add(Box.createHorizontalStrut(20));

        minDiastolicField = new JTextField(3);
        maxDiastolicField = new JTextField(3);
        searchInnerPanel3.add(new JLabel("舒张压:"));
        searchInnerPanel3.add(minDiastolicField);
        searchInnerPanel3.add(new JLabel("-"));
        searchInnerPanel3.add(maxDiastolicField);

        //第四五六部分
        JPanel searchInnerPanel4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel searchInnerPanel5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel searchInnerPanel6 = new JPanel(new FlowLayout(FlowLayout.LEFT));

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

        searchInnerPanel4.add(new JLabel("高血压史"));
        searchInnerPanel4.add(hypertensionComboBox);
        searchInnerPanel4.add(Box.createHorizontalStrut(20));
        searchInnerPanel4.add(new JLabel("卒中史"));
        searchInnerPanel4.add(strokeComboBox);
        searchInnerPanel4.add(Box.createHorizontalStrut(20));
        searchInnerPanel4.add(new JLabel("糖尿病史"));
        searchInnerPanel4.add(diabetesComboBox);
        searchInnerPanel4.add(Box.createHorizontalStrut(20));
        searchInnerPanel4.add(new JLabel("房颤史"));
        searchInnerPanel4.add(atrialFibComboBox);
        searchInnerPanel4.add(Box.createHorizontalStrut(20));
        searchInnerPanel4.add(new JLabel("冠心病史"));
        searchInnerPanel4.add(coronaryComboBox);

        searchInnerPanel5.add(new JLabel("吸烟史"));
        searchInnerPanel5.add(smokingComboBox);
        searchInnerPanel5.add(Box.createHorizontalStrut(20));
        searchInnerPanel5.add(new JLabel("饮酒史"));
        searchInnerPanel5.add(drinkingComboBox);
        searchInnerPanel5.add(Box.createHorizontalStrut(20));
        searchInnerPanel5.add(new JLabel("止血治疗"));
        searchInnerPanel5.add(hemostaticComboBox);
        searchInnerPanel5.add(Box.createHorizontalStrut(20));
        searchInnerPanel5.add(new JLabel("颅压降低治疗"));
        searchInnerPanel5.add(icpReductionComboBox);
        searchInnerPanel5.add(Box.createHorizontalStrut(20));
        searchInnerPanel5.add(new JLabel("降压治疗"));
        searchInnerPanel5.add(antihypertensiveComboBox);

        searchInnerPanel6.add(new JLabel("镇静镇痛"));
        searchInnerPanel6.add(sedationComboBox);
        searchInnerPanel6.add(Box.createHorizontalStrut(20));
        searchInnerPanel6.add(new JLabel("止吐胃保护"));
        searchInnerPanel6.add(antiemeticComboBox);
        searchInnerPanel6.add(Box.createHorizontalStrut(20));
        searchInnerPanel6.add(new JLabel("神经营养"));
        searchInnerPanel6.add(trophicNerveComboBox);

        // 查询按钮单独放底下
        JPanel searchButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchButtonPanel.setOpaque(false);
        searchButton = new JButton("查询");
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

        addButton = new JButton("新增患者");
        addButton.addActionListener(e -> showAddDialog());
        buttonPanel.add(addButton);

        // 新增修改按钮
        JButton editButton = new JButton("修改患者");
        editButton.addActionListener(e -> showEditDialog());
        buttonPanel.add(editButton);

        deleteButton = new JButton("删除患者");
        deleteButton.addActionListener(e -> deletePatient());
        buttonPanel.add(deleteButton);

        // saveButton = new JButton("保存修改");
        // saveButton.addActionListener(e -> saveTableChanges());
        // buttonPanel.add(saveButton);

        backButton = new JButton("返回菜单");
        backButton.addActionListener(e -> backToMenu());
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }
    private void showAddDialog() {
        JDialog addPatientDialog = new JDialog();
        addPatientDialog.setTitle("新增患者");

        // 创建输入框
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField idField = new JTextField(10);
        JTextField ageField = new JTextField(10);
        JComboBox<String> genderComboBox = new JComboBox<>(new String[]{"男", "女"});
        JTextField mrsField = new JTextField(10);
        JTextField onsetField = new JTextField(10);
        JTextField systolicField = new JTextField(10);
        JTextField diastolicField = new JTextField(10);

        // 病史字段：使用复选框或下拉框
        JCheckBox hypertensionCheckBox = new JCheckBox("高血压史");
        JCheckBox strokeCheckBox = new JCheckBox("卒中史");
        JCheckBox diabetesCheckBox = new JCheckBox("糖尿病史");
        JCheckBox atrialFibCheckBox = new JCheckBox("房颤史");
        JCheckBox coronaryCheckBox = new JCheckBox("冠心病史");
        JCheckBox smokingCheckBox = new JCheckBox("吸烟史");
        JCheckBox drinkingCheckBox = new JCheckBox("饮酒史");

        //治疗措施
        JCheckBox hemostaticCheckBox = new JCheckBox("止血治疗");
        JCheckBox icpReductionCheckBox = new JCheckBox("颅内压降低治疗");
        JCheckBox antihypertensiveCheckBox = new JCheckBox("降压治疗");
        JCheckBox sedationCheckBox = new JCheckBox("镇静镇痛");
        JCheckBox antiemeticCheckBox = new JCheckBox("止吐胃保护");
        JCheckBox trophicNerveCheckBox = new JCheckBox("神经营养");


        panel.add(new JLabel("患者ID:"));
        panel.add(idField);
        panel.add(new JLabel("年龄:"));
        panel.add(ageField);
        panel.add(new JLabel("性别:"));
        panel.add(genderComboBox);
        panel.add(new JLabel("MRS评分:"));
        panel.add(mrsField);
        panel.add(new JLabel("发病到影像时间(h):"));
        panel.add(onsetField);
        panel.add(new JLabel("收缩压:"));
        panel.add(systolicField);
        panel.add(new JLabel("舒张压:"));
        panel.add(diastolicField);

        panel.add(new JLabel("病史:"));
        panel.add(new JLabel());  // 占位符
        panel.add(hypertensionCheckBox);
        panel.add(strokeCheckBox);
        panel.add(diabetesCheckBox);
        panel.add(atrialFibCheckBox);
        panel.add(coronaryCheckBox);
        panel.add(smokingCheckBox);
        panel.add(drinkingCheckBox);
        panel.add(new JLabel("治疗措施:"));
        panel.add(new JLabel());  // 占位符
        panel.add(hemostaticCheckBox);
        panel.add(icpReductionCheckBox);
        panel.add(antihypertensiveCheckBox);
        panel.add(sedationCheckBox);
        panel.add(antiemeticCheckBox);
        panel.add(trophicNerveCheckBox);

        // 创建保存按钮
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("保存");
        saveButton.addActionListener(e -> {
            try {
                Patient newPatient = new Patient();
                newPatient.setId(idField.getText());
                newPatient.setAge(Integer.parseInt(ageField.getText()));
                String gender = genderComboBox.getSelectedItem().equals("男") ? "M" : "F";
                newPatient.setGender(gender);
                // newPatient.setGender((String) genderComboBox.getSelectedItem());
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
        panel.add(buttonPanel);

        addPatientDialog.add(panel);
        addPatientDialog.setSize(600, 800);
        addPatientDialog.setVisible(true);
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

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ID字段设为不可编辑
        JTextField idField = new JTextField(patient.getId());
        idField.setEditable(false);
        JTextField ageField = new JTextField(String.valueOf(patient.getAge()));
        JComboBox<String> genderComboBox = new JComboBox<>(new String[]{"男", "女"});
        genderComboBox.setSelectedItem(patient.getGender().equals("M") ? "男" : "女");
        JTextField mrsField = new JTextField(String.valueOf(patient.getMrsScore()));
        JTextField onsetField = new JTextField(String.valueOf(patient.getOnsetToImagingHours()));
        JTextField systolicField = new JTextField(String.valueOf(patient.getSystolicPressure()));
        JTextField diastolicField = new JTextField(String.valueOf(patient.getDiastolicPressure()));

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

        // 添加组件到面板（与新增对话框相同的布局）
        panel.add(new JLabel("患者ID:"));
        panel.add(idField);
        panel.add(new JLabel("年龄:"));
        panel.add(ageField);
        panel.add(new JLabel("性别:"));
        panel.add(genderComboBox);
        panel.add(new JLabel("MRS评分:"));
        panel.add(mrsField);
        panel.add(new JLabel("发病到影像时间(h):"));
        panel.add(onsetField);
        panel.add(new JLabel("收缩压:"));
        panel.add(systolicField);
        panel.add(new JLabel("舒张压:"));
        panel.add(diastolicField);

        panel.add(new JLabel("病史:"));
        panel.add(new JLabel());
        panel.add(hypertensionCheckBox);
        panel.add(strokeCheckBox);
        panel.add(diabetesCheckBox);
        panel.add(atrialFibCheckBox);
        panel.add(coronaryCheckBox);
        panel.add(smokingCheckBox);
        panel.add(drinkingCheckBox);
        panel.add(new JLabel("治疗措施:"));
        panel.add(new JLabel());
        panel.add(hemostaticCheckBox);
        panel.add(icpReductionCheckBox);
        panel.add(antihypertensiveCheckBox);
        panel.add(sedationCheckBox);
        panel.add(antiemeticCheckBox);
        panel.add(trophicNerveCheckBox);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("保存修改");
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
        panel.add(buttonPanel);

        editDialog.add(panel);
        editDialog.setSize(600, 800);
        editDialog.setModal(true);
        editDialog.setVisible(true);
    }

    private void searchPatients() {
        // 创建条件字典
        Map<String, Object> searchCriteria = new HashMap<>();

        // 添加基本信息到字典
        searchCriteria.put("id",ParseUtils.parseString(idField.getText().trim()));
        searchCriteria.put("gender", parseGender(genderComboBox));
        searchCriteria.put("minAge", ParseUtils.parseInteger(minAgeField.getText().trim()));
        searchCriteria.put("maxAge", ParseUtils.parseInteger(maxAgeField.getText().trim()));
        searchCriteria.put("minMrs", ParseUtils.parseInteger(minMrsField.getText().trim()));
        searchCriteria.put("maxMrs", ParseUtils.parseInteger(maxMrsField.getText().trim()));
        searchCriteria.put("minOnset",ParseUtils.parseDouble(minOnsetField.getText().trim()));
        searchCriteria.put("maxOnset",ParseUtils.parseDouble(maxOnsetField.getText().trim()));
        searchCriteria.put("minSystolic",ParseUtils.parseInteger(minSystolicField.getText().trim()));
        searchCriteria.put("maxSystolic",ParseUtils.parseInteger(maxSystolicField.getText().trim()));
        searchCriteria.put("minDiastolic",ParseUtils.parseInteger(minDiastolicField.getText().trim()));
        searchCriteria.put("maxDiastolic",ParseUtils.parseInteger(maxDiastolicField.getText().trim()));
        // 添加病史条件到字典，假设ComboBox的值为字符串类型
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
        System.out.println(searchCriteria);
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
        tableModel=newModel;
    }

    private Integer getHistoryCondition(JComboBox<String> comboBox) {
        String selected = (String) comboBox.getSelectedItem();
        if ("不限".equals(selected)) return null;
        return "有".equals(selected) ? 1 : 0;
    }
    private String parseGender(JComboBox<String> comboBox){
        String selected = (String) comboBox.getSelectedItem();
        if("全部".equals(selected)) return null;
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

    private void saveTableChanges() {
        PatientService patientService = mainFrame.getPatientService();
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            String id = ParseUtils.parseString(tableModel.getValueAt(row, 0));
            if (!modifiedPatientIds.contains(id)) {
                continue; // 跳过没改过的
            }

            try {
                Patient patient = new Patient();
                patient.setId(id);
                patient.setAge(ParseUtils.parseInteger(tableModel.getValueAt(row, 1)));
                patient.setGender(ParseUtils.parseString(tableModel.getValueAt(row, 2)));
                patient.setMrsScore(ParseUtils.parseInteger(tableModel.getValueAt(row, 3)));
                patient.setHypertensionHistory(ParseUtils.parseInteger(tableModel.getValueAt(row, 4)));
                patient.setStrokeHistory(ParseUtils.parseInteger(tableModel.getValueAt(row, 5)));
                patient.setDiabetesHistory(ParseUtils.parseInteger(tableModel.getValueAt(row, 6)));
                patient.setAfHistory(ParseUtils.parseInteger(tableModel.getValueAt(row, 7)));
                patient.setChdHistory(ParseUtils.parseInteger(tableModel.getValueAt(row, 8)));
                patient.setSmokingHistory(ParseUtils.parseInteger(tableModel.getValueAt(row, 9)));
                patient.setDrinkingHistory(ParseUtils.parseInteger(tableModel.getValueAt(row, 10)));
                patient.setOnsetToImagingHours(ParseUtils.parseDouble(tableModel.getValueAt(row, 11)));
                patient.setSystolicPressure(ParseUtils.parseInteger(tableModel.getValueAt(row, 12)));
                patient.setDiastolicPressure(ParseUtils.parseInteger(tableModel.getValueAt(row, 13)));
                patient.setHemostaticTherapy(ParseUtils.parseInteger(tableModel.getValueAt(row, 14)));
                patient.setIcpReductionTherapy(ParseUtils.parseInteger(tableModel.getValueAt(row, 15)));
                patient.setAntihypertensiveTherapy(ParseUtils.parseInteger(tableModel.getValueAt(row, 16)));
                patient.setSedationAnalgesia(ParseUtils.parseInteger(tableModel.getValueAt(row, 17)));
                patient.setAntiemeticGastricProtection(ParseUtils.parseInteger(tableModel.getValueAt(row, 18)));
                patient.setNeurotrophicTherapy(ParseUtils.parseInteger(tableModel.getValueAt(row, 19)));

                patientService.updatePatient(patient);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "保存失败！请检查数据格式。");
                return;
            }
        }

        modifiedPatientIds.clear(); // 清空修改记录
        JOptionPane.showMessageDialog(this, "保存成功！");
    }

    private void backToMenu() {
        mainFrame.showMainMenuPanel();
    }
}