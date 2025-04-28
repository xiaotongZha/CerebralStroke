package service;

import dao.PatientDao;
import model.Patient;

import java.util.List;
import java.util.Map;

public class PatientService {
    private PatientDao patientDao = new PatientDao();

    public List<Patient> getAllPatients() {
        return patientDao.findAll();
    }

    public void addPatient(Patient patient) {
        patientDao.insert(patient);
    }

    public void deletePatient(String id) {
        patientDao.deleteById(id);
    }

    public void updatePatient(Patient patient) {
        patientDao.update(patient);
    }

    public List<Patient> searchPatients(Map<String,Object> searchCriteria) {
        return patientDao.search(searchCriteria);
    }
}
