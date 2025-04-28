package dao;

import model.Patient;

import java.util.*;
import java.util.stream.Collectors;

public class PatientDao {
    private List<Patient> patientList = new ArrayList<>();

    public PatientDao() {
        // 初始化10条假数据
        for (int i = 1; i <= 10; i++) {
            Patient p = new Patient();
            p.setId("sub"+i);
            p.setAge(20 + i); // 年龄20到29
            p.setGender(i % 2 == 0 ? "M" : "F"); // 偶数男，奇数女
            p.setMrsScore(i % 6); // 0~5的mRS评分
            p.setHypertensionHistory(i % 2); // 0或1
            p.setStrokeHistory(i % 3 == 0 ? 1 : 0);
            p.setDiabetesHistory(i % 4 == 0 ? 1 : 0);
            p.setAfHistory(i % 5 == 0 ? 1 : 0);
            p.setChdHistory(i % 2);
            p.setSmokingHistory(i % 3 == 0 ? 1 : 0);
            p.setDrinkingHistory(i % 2);
            p.setOnsetToImagingHours(1.5 + i); // 发病到影像小时
            p.setSystolicPressure(120 + i);
            p.setDiastolicPressure(80 + i);
            p.setHemostaticTherapy(i % 2);
            p.setIcpReductionTherapy(i % 2);
            p.setAntihypertensiveTherapy(i % 2);
            p.setSedationAnalgesia(i % 2);
            p.setAntiemeticGastricProtection(i % 2);
            p.setNeurotrophicTherapy(i % 2);
            patientList.add(p);
        }
    }

    public List<Patient> findAll() {
        return new ArrayList<>(patientList);
    }

    public void insert(Patient patient) {
        patientList.add(patient);
    }

    public void deleteById(String id) {
        patientList.removeIf(p -> p.getId().equals(id));
    }

    public void update(Patient updatedPatient) {
        for (int i = 0; i < patientList.size(); i++) {
            if (patientList.get(i).getId().equals(updatedPatient.getId())) {
                patientList.set(i, updatedPatient);
                break;
            }
        }
    }

    // 重点：修改后的 search 方法，接受 Map<String, Object> 参数
    public List<Patient> search(Map<String, Object> conditions) {
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

    // 辅助方法，处理ComboBox的筛选，支持 “全部”
    private boolean filterCondition(Integer patientValue, Object conditionValue) {
        if (conditionValue == null || "全部".equals(conditionValue)) {
            return true; // 没有限制，直接通过
        }
        if (patientValue == null) {
            return false; // 病人数据缺失，不符合
        }
        try {
            int cv = Integer.parseInt(conditionValue.toString());
            return patientValue.equals(cv); // 精确匹配
        } catch (NumberFormatException e) {
            return false;
        }
    }
}