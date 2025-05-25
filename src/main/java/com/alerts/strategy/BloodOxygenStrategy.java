package com.alerts.strategy;

import com.alerts.Alert;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import com.alerts.factory.BloodOxygenAlertFactory;

import java.util.List;

public class BloodOxygenStrategy implements AlertStrategy{
    private final int THRESHOLD = 92;


    public Alert checkAlert(Patient patient, String condition, long timestamp) {
        String patientId = String.valueOf(patient.getPatientId());
        BloodOxygenAlertFactory factory = new BloodOxygenAlertFactory();
        List<PatientRecord> record = patient.getRecords(0, Long.MAX_VALUE, condition);

        if (record.isEmpty()) {
            return null;
        }

        if (record.get(record.size() - 1).getMeasurementValue() < THRESHOLD) {
            return factory.createAlert(patientId, "Blood Oxygen Level Low", timestamp);
        }
        return null;
    }
}
