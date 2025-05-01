package model;

public class Patient {
    private String id;
    private Integer age;
    private String gender;
    private Integer mrsScore;
    private Integer hypertensionHistory;
    private Integer strokeHistory;
    private Integer diabetesHistory;
    private Integer afHistory;
    private Integer chdHistory;
    private Integer smokingHistory;
    private Integer drinkingHistory;
    private Double onsetToImagingHours;
    private Integer systolicPressure;
    private Integer diastolicPressure;
    private Integer hemostaticTherapy;
    private Integer icpReductionTherapy;
    private Integer antihypertensiveTherapy;
    private Integer sedationAnalgesia;
    private Integer antiemeticGastricProtection;
    private Integer neurotrophicTherapy;

    // Getter & Setter
    public void setAge(Integer age) {
        this.age = age;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setHypertensionHistory(Integer hypertensionHistory) {
        this.hypertensionHistory = hypertensionHistory;
    }

    public void setMrsScore(Integer mrsScore) {
        this.mrsScore = mrsScore;
    }

    public void setStrokeHistory(Integer strokeHistory) {
        this.strokeHistory = strokeHistory;
    }

    public void setDiabetesHistory(Integer diabetesHistory) {
        this.diabetesHistory = diabetesHistory;
    }

    public void setAfHistory(Integer afHistory) {
        this.afHistory = afHistory;
    }

    public void setChdHistory(Integer chdHistory) {
        this.chdHistory = chdHistory;
    }

    public void setSmokingHistory(Integer smokingHistory) {
        this.smokingHistory = smokingHistory;
    }

    public void setDrinkingHistory(Integer drinkingHistory) {
        this.drinkingHistory = drinkingHistory;
    }

    public void setOnsetToImagingHours(Double onsetToImagingHours) {
        this.onsetToImagingHours = onsetToImagingHours;
    }

    public void setSystolicPressure(Integer systolicPressure) {
        this.systolicPressure = systolicPressure;
    }

    public void setDiastolicPressure(Integer diastolicPressure) {
        this.diastolicPressure = diastolicPressure;
    }

    public void setHemostaticTherapy(Integer hemostaticTherapy) {
        this.hemostaticTherapy = hemostaticTherapy;
    }

    public void setIcpReductionTherapy(Integer icpReductionTherapy) {
        this.icpReductionTherapy = icpReductionTherapy;
    }

    public void setAntihypertensiveTherapy(Integer antihypertensiveTherapy) {
        this.antihypertensiveTherapy = antihypertensiveTherapy;
    }

    public void setSedationAnalgesia(Integer sedationAnalgesia) {
        this.sedationAnalgesia = sedationAnalgesia;
    }

    public void setAntiemeticGastricProtection(Integer antiemeticGastricProtection) {
        this.antiemeticGastricProtection = antiemeticGastricProtection;
    }

    public void setNeurotrophicTherapy(Integer neurotrophicTherapy) {
        this.neurotrophicTherapy = neurotrophicTherapy;
    }

    public Integer getAge() {
        return age;
    }

    public Integer getMrsScore() {
        return mrsScore;
    }

    public String getGender() {
        return gender;
    }

    public String getId() {
        return id;
    }

    public Integer getHypertensionHistory() {
        return hypertensionHistory;
    }

    public Integer getStrokeHistory() {
        return strokeHistory;
    }

    public Integer getAfHistory() {
        return afHistory;
    }

    public Integer getDiabetesHistory() {
        return diabetesHistory;
    }

    public Integer getChdHistory() {
        return chdHistory;
    }

    public Integer getSmokingHistory() {
        return smokingHistory;
    }

    public Integer getDrinkingHistory() {
        return drinkingHistory;
    }

    public Double getOnsetToImagingHours() {
        return onsetToImagingHours;
    }

    public Integer getSystolicPressure() {
        return systolicPressure;
    }

    public Integer getDiastolicPressure() {
        return diastolicPressure;
    }

    public Integer getHemostaticTherapy() {
        return hemostaticTherapy;
    }

    public Integer getIcpReductionTherapy() {
        return icpReductionTherapy;
    }

    public Integer getAntihypertensiveTherapy() {
        return antihypertensiveTherapy;
    }

    public Integer getSedationAnalgesia() {
        return sedationAnalgesia;
    }

    public Integer getAntiemeticGastricProtection() {
        return antiemeticGastricProtection;
    }

    public Integer getNeurotrophicTherapy() {
        return neurotrophicTherapy;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id='" + id +
                ", age=" + age +
                ", gender='" + gender +
                ", mrsScore=" + mrsScore +
                ", hypertensionHistory=" + hypertensionHistory +
                ", strokeHistory=" + strokeHistory +
                ", diabetesHistory=" + diabetesHistory +
                ", afHistory=" + afHistory +
                ", chdHistory=" + chdHistory +
                ", smokingHistory=" + smokingHistory +
                ", drinkingHistory=" + drinkingHistory +
                ", onsetToImagingHours=" + onsetToImagingHours +
                ", systolicPressure=" + systolicPressure +
                ", diastolicPressure=" + diastolicPressure +
                ", hemostaticTherapy=" + hemostaticTherapy +
                ", icpReductionTherapy=" + icpReductionTherapy +
                ", antihypertensiveTherapy=" + antihypertensiveTherapy +
                ", sedationAnalgesia=" + sedationAnalgesia +
                ", antiemeticGastricProtection=" + antiemeticGastricProtection +
                ", neurotrophicTherapy=" + neurotrophicTherapy +
                '}';
    }
}
