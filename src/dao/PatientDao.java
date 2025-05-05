package dao;

import model.Patient;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class PatientDao {
    private static final String URL = "jdbc:mysql://101.37.17.111:3306/CerebralStroke?useSSL=false&serverTimezone=UTC";
    private static final String USER = "zxt";
    private static final String PASSWORD = "123456";

    public List<Patient> findAll() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM Patient ORDER BY id ASC";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs));
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return patients;
    }
    private boolean filterCondition(Integer patientHistory, Object condition) {
        // 根据你的业务逻辑来实现这个方法
        // 例如：
        if (condition == null) {
            return true;
        }
        // 假设condition是一个Integer
        Integer conditionValue = (Integer) condition;
        return patientHistory != null && patientHistory <= conditionValue;
    }

    public void insert(Patient patient) {
        String sql = "INSERT INTO Patient (id, age, gender, mrsScore, hypertensionHistory, strokeHistory, "
                + "diabetesHistory, afHistory, chdHistory, smokingHistory, drinkingHistory, "
                + "onsetToImagingHours, systolicPressure, diastolicPressure, hemostaticTherapy, "
                + "icpReductionTherapy, antihypertensiveTherapy, sedationAnalgesia, "
                + "antiemeticGastricProtection, neurotrophicTherapy) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            System.out.println("Inserting patient:");
            System.out.println(patient.toString());

            setPatientParameters(pstmt, patient);
            int rows = pstmt.executeUpdate();
            System.out.println("Affected rows: " + rows);
            List<Patient> patients = findAll();
            System.out.println("Current Patients: " + patients);

        } catch (SQLException e) {
            System.err.println("完整错误信息:");
            e.printStackTrace();
            System.err.println("SQL状态码: " + e.getSQLState());
            System.err.println("错误代码: " + e.getErrorCode());
            System.err.println("错误信息: " + e.getMessage());
        }
    }

    public void deleteById(String id) {
        String sql = "DELETE FROM Patient WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void update(Patient updatedPatient) {
        String sql = "UPDATE Patient SET "
                + "age = ?, gender = ?, mrsScore = ?, hypertensionHistory = ?, strokeHistory = ?, "
                + "diabetesHistory = ?, afHistory = ?, chdHistory = ?, smokingHistory = ?, "
                + "drinkingHistory = ?, onsetToImagingHours = ?, systolicPressure = ?, "
                + "diastolicPressure = ?, hemostaticTherapy = ?, icpReductionTherapy = ?, "
                + "antihypertensiveTherapy = ?, sedationAnalgesia = ?, antiemeticGastricProtection = ?, "
                + "neurotrophicTherapy = ? "
                + "WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 1) 绑定 SET 部分的 19 个参数（从 1 到 19）
            pstmt.setInt(1,  updatedPatient.getAge());
            pstmt.setString(2, updatedPatient.getGender());
            pstmt.setInt(3,  updatedPatient.getMrsScore());
            pstmt.setInt(4,  updatedPatient.getHypertensionHistory());
            pstmt.setInt(5,  updatedPatient.getStrokeHistory());
            pstmt.setInt(6,  updatedPatient.getDiabetesHistory());
            pstmt.setInt(7,  updatedPatient.getAfHistory());
            pstmt.setInt(8,  updatedPatient.getChdHistory());
            pstmt.setInt(9,  updatedPatient.getSmokingHistory());
            pstmt.setInt(10, updatedPatient.getDrinkingHistory());
            pstmt.setDouble(11, updatedPatient.getOnsetToImagingHours());
            pstmt.setInt(12, updatedPatient.getSystolicPressure());      // 用 setInt 而不是 setDouble
            pstmt.setInt(13, updatedPatient.getDiastolicPressure());
            pstmt.setInt(14, updatedPatient.getHemostaticTherapy());
            pstmt.setInt(15, updatedPatient.getIcpReductionTherapy());
            pstmt.setInt(16, updatedPatient.getAntihypertensiveTherapy());
            pstmt.setInt(17, updatedPatient.getSedationAnalgesia());
            pstmt.setInt(18, updatedPatient.getAntiemeticGastricProtection());
            pstmt.setInt(19, updatedPatient.getNeurotrophicTherapy());

            // 2) 绑定 WHERE id = ? 的第 20 个参数
            pstmt.setString(20, updatedPatient.getId());

            int rows = pstmt.executeUpdate();
            System.out.println("更新影响行数: " + rows);

        } catch (SQLException e) {
            handleSQLException(e);
        }
    }


    public List<Patient> search(Map<String, Object> conditions) {
        List<Patient> patientList = findAll();
        return patientList.stream()
                .filter(p-> {
                    String id = (String) conditions.get("id");
                    return id.isEmpty() || id.equals(p.getId());
                })
                .filter(p -> {
                    String gender = (String) conditions.get("gender");
                    return gender == null || gender.equals(p.getGender());
                })
                .filter(p -> {
                    Integer minAge = (Integer) conditions.get("minAge");
                    return minAge == null || (p.getAge() != null && p.getAge() >= minAge);
                })
                .filter(p -> {
                    Integer maxAge = (Integer) conditions.get("maxAge");
                    return maxAge == null || (p.getAge() != null && p.getAge() <= maxAge);
                })
                .filter(p -> {
                    Integer minMrs = (Integer) conditions.get("minMrs");
                    return minMrs == null || (p.getMrsScore() != null && p.getMrsScore() >= minMrs);
                })
                .filter(p -> {
                    Integer maxMrs = (Integer) conditions.get("maxMrs");
                    return maxMrs == null || (p.getMrsScore() != null && p.getMrsScore() <= maxMrs);
                })
                .filter(p -> {
                    Double minOnset = (Double) conditions.get("minOnset");
                    return minOnset == null || (p.getOnsetToImagingHours() != null && p.getOnsetToImagingHours() >= minOnset);
                })
                .filter(p -> {
                    Double maxOnset = (Double) conditions.get("maxOnset");
                    return maxOnset == null || (p.getOnsetToImagingHours() != null && p.getOnsetToImagingHours() <= maxOnset);
                })
                .filter(p -> {
                    Integer minSystolic = (Integer) conditions.get("minSystolic");
                    return minSystolic == null || (p.getSystolicPressure() != null && p.getSystolicPressure() >= minSystolic);
                })
                .filter(p -> {
                    Integer maxSystolic = (Integer) conditions.get("maxSystolic");
                    return maxSystolic == null || (p.getSystolicPressure() != null && p.getSystolicPressure() <= maxSystolic);
                })
                .filter(p -> {
                    Integer minDiastolic = (Integer) conditions.get("minDiastolic");
                    return minDiastolic == null || (p.getDiastolicPressure() != null && p.getDiastolicPressure() >= minDiastolic);
                })
                .filter(p -> {
                    Integer maxDiastolic = (Integer) conditions.get("maxDiastolic");
                    return maxDiastolic == null || (p.getDiastolicPressure() != null && p.getDiastolicPressure() <= maxDiastolic);
                })
                // 以下是新增的病史条件过滤
                .filter(p -> filterCondition(p.getHypertensionHistory(), conditions.get("hypertension")))
                .filter(p -> filterCondition(p.getStrokeHistory(), conditions.get("stroke")))
                .filter(p -> filterCondition(p.getDiabetesHistory(), conditions.get("diabetes")))
                .filter(p -> filterCondition(p.getAfHistory(), conditions.get("atrialFib")))
                .filter(p -> filterCondition(p.getChdHistory(), conditions.get("coronary")))
                .filter(p -> filterCondition(p.getSmokingHistory(), conditions.get("smoking")))
                .filter(p -> filterCondition(p.getDrinkingHistory(), conditions.get("drinking")))
                .filter(p -> filterCondition(p.getHemostaticTherapy(), conditions.get("hemostatic")))
                .filter(p -> filterCondition(p.getIcpReductionTherapy(), conditions.get("icpReduction")))
                .filter(p -> filterCondition(p.getAntihypertensiveTherapy(), conditions.get("antihypertensive")))
                .filter(p -> filterCondition(p.getSedationAnalgesia(), conditions.get("sedation")))
                .filter(p -> filterCondition(p.getAntiemeticGastricProtection(), conditions.get("antiemetic")))
                .filter(p -> filterCondition(p.getNeurotrophicTherapy(), conditions.get("trophicNerve")))
                .collect(Collectors.toList());
    }

    public Patient findById(String patientId) {
        String sql = "SELECT * FROM Patient WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, patientId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPatient(rs);
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return null;
    }

    private Patient mapResultSetToPatient(ResultSet rs) throws SQLException {
        Patient patient = new Patient();
        patient.setId(rs.getString("id"));
        patient.setAge(rs.getInt("age"));
        patient.setGender(rs.getString("gender"));
        patient.setMrsScore(rs.getInt("mrsScore"));
        patient.setHypertensionHistory(rs.getInt("hypertensionHistory"));
        patient.setStrokeHistory(rs.getInt("strokeHistory"));
        patient.setDiabetesHistory(rs.getInt("diabetesHistory"));
        patient.setAfHistory(rs.getInt("afHistory"));
        patient.setChdHistory(rs.getInt("chdHistory"));
        patient.setSmokingHistory(rs.getInt("smokingHistory"));
        patient.setDrinkingHistory(rs.getInt("drinkingHistory"));
        patient.setOnsetToImagingHours(rs.getDouble("onsetToImagingHours"));
        patient.setSystolicPressure(rs.getInt("systolicPressure"));
        patient.setDiastolicPressure(rs.getInt("diastolicPressure"));
        patient.setHemostaticTherapy(rs.getInt("hemostaticTherapy"));
        patient.setIcpReductionTherapy(rs.getInt("icpReductionTherapy"));
        patient.setAntihypertensiveTherapy(rs.getInt("antihypertensiveTherapy"));
        patient.setSedationAnalgesia(rs.getInt("sedationAnalgesia"));
        patient.setAntiemeticGastricProtection(rs.getInt("antiemeticGastricProtection"));
        patient.setNeurotrophicTherapy(rs.getInt("neurotrophicTherapy"));
        return patient;
    }

    private void setPatientParameters(PreparedStatement pstmt, Patient patient) throws SQLException {
        pstmt.setString(1, patient.getId());
        pstmt.setInt(2, patient.getAge());
        pstmt.setString(3, patient.getGender());
        pstmt.setInt(4, patient.getMrsScore());
        pstmt.setInt(5, patient.getHypertensionHistory());
        pstmt.setInt(6, patient.getStrokeHistory());
        pstmt.setInt(7, patient.getDiabetesHistory());
        pstmt.setInt(8, patient.getAfHistory());
        pstmt.setInt(9, patient.getChdHistory());
        pstmt.setInt(10, patient.getSmokingHistory());
        pstmt.setInt(11, patient.getDrinkingHistory());
        pstmt.setDouble(12, patient.getOnsetToImagingHours());
        pstmt.setDouble(13, patient.getSystolicPressure());
        pstmt.setDouble(14, patient.getDiastolicPressure());
        pstmt.setInt(15, patient.getHemostaticTherapy());
        pstmt.setInt(16, patient.getIcpReductionTherapy());
        pstmt.setInt(17, patient.getAntihypertensiveTherapy());
        pstmt.setInt(18, patient.getSedationAnalgesia());
        pstmt.setInt(19, patient.getAntiemeticGastricProtection());
        pstmt.setInt(20, patient.getNeurotrophicTherapy());
    }

    private void buildSearchConditions(StringBuilder sqlBuilder, List<Object> parameters, Map<String, Object> conditions) {
        if (conditions.containsKey("id") && !((String) conditions.get("id")).isEmpty()) {
            sqlBuilder.append(" AND id = ?");
            parameters.add(conditions.get("id"));
        }

        if (conditions.containsKey("gender") && conditions.get("gender") != null) {
            sqlBuilder.append(" AND gender = ?");
            parameters.add(conditions.get("gender"));
        }

        if (conditions.containsKey("minAge")) {
            sqlBuilder.append(" AND age >= ?");
            parameters.add(conditions.get("minAge"));
        }
        if (conditions.containsKey("maxAge")) {
            sqlBuilder.append(" AND age <= ?");
            parameters.add(conditions.get("maxAge"));
        }

        // 修正字段名
        if (conditions.containsKey("minMrs")) {
            sqlBuilder.append(" AND mrsScore >= ?");
            parameters.add(conditions.get("minMrs"));
        }
        if (conditions.containsKey("maxMrs")) {
            sqlBuilder.append(" AND mrsScore <= ?");
            parameters.add(conditions.get("maxMrs"));
        }

        if (conditions.containsKey("minOnset")) {
            sqlBuilder.append(" AND onsetToImagingHours >= ?");
            parameters.add(conditions.get("minOnset"));
        }
        if (conditions.containsKey("maxOnset")) {
            sqlBuilder.append(" AND onsetToImagingHours <= ?");
            parameters.add(conditions.get("maxOnset"));
        }

        if (conditions.containsKey("minSystolic")) {
            sqlBuilder.append(" AND systolicPressure >= ?");
            parameters.add(conditions.get("minSystolic"));
        }
        if (conditions.containsKey("maxSystolic")) {
            sqlBuilder.append(" AND systolicPressure <= ?");
            parameters.add(conditions.get("maxSystolic"));
        }
        if (conditions.containsKey("minDiastolic")) {
            sqlBuilder.append(" AND diastolicPressure >= ?");
            parameters.add(conditions.get("minDiastolic"));
        }
        if (conditions.containsKey("maxDiastolic")) {
            sqlBuilder.append(" AND diastolicPressure <= ?");
            parameters.add(conditions.get("maxDiastolic"));
        }

        // 修正病史字段名
        addMedicalCondition(sqlBuilder, parameters, conditions, "hypertension", "hypertensionHistory");
        addMedicalCondition(sqlBuilder, parameters, conditions, "stroke", "strokeHistory");
        addMedicalCondition(sqlBuilder, parameters, conditions, "diabetes", "diabetesHistory");
        addMedicalCondition(sqlBuilder, parameters, conditions, "atrialFib", "afHistory");
        addMedicalCondition(sqlBuilder, parameters, conditions, "coronary", "chdHistory");
        addMedicalCondition(sqlBuilder, parameters, conditions, "smoking", "smokingHistory");
        addMedicalCondition(sqlBuilder, parameters, conditions, "drinking", "drinkingHistory");
        addMedicalCondition(sqlBuilder, parameters, conditions, "hemostatic", "hemostaticTherapy");
        addMedicalCondition(sqlBuilder, parameters, conditions, "icpReduction", "icpReductionTherapy");
        addMedicalCondition(sqlBuilder, parameters, conditions, "antihypertensive", "antihypertensiveTherapy");
        addMedicalCondition(sqlBuilder, parameters, conditions, "sedation", "sedationAnalgesia");
        addMedicalCondition(sqlBuilder, parameters, conditions, "antiemetic", "antiemeticGastricProtection");
        addMedicalCondition(sqlBuilder, parameters, conditions, "trophicNerve", "neurotrophicTherapy");
    }

    private void addMedicalCondition(StringBuilder sqlBuilder, List<Object> parameters,
                                     Map<String, Object> conditions, String conditionKey, String columnName) {
        if (conditions.containsKey(conditionKey)) {
            Object value = conditions.get(conditionKey);
            if (value != null && !"全部".equals(value)) {
                sqlBuilder.append(" AND ").append(columnName).append(" = ?");
                parameters.add(Integer.parseInt(value.toString()));
            }
        }
    }


    private void handleSQLException(SQLException e) {
        System.err.println("数据库操作失败: " + e.getMessage());
        e.printStackTrace();
    }
}