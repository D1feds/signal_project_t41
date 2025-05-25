package com.alerts.strategy;

import com.alerts.Alert;
import com.alerts.factory.ECGAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import java.util.List;


public class ECGAlertStrategy implements AlertStrategy{


    @Override
    public Alert alert(Patient patient, String condition, long timestamp) {


        String patientId = String.valueOf(patient.getPatientId());
        ECGAlertFactory factory = new ECGAlertFactory();

        List<PatientRecord> record = patient.getRecords(0, Long.MAX_VALUE, condition);



        if (record.isEmpty()) {
            return null;
        }

        int THRESHOLD_HIGH = 100;
        int THRESHOLD_LOW = 50;


        if (record.get(record.size()-1).getMeasurementValue() > THRESHOLD_HIGH) {
            return factory.createAlert(patientId, "ECG_ALERT_HIGH", timestamp);
        }

        if (record.get(record.size()-1).getMeasurementValue() < THRESHOLD_LOW) {
            return factory.createAlert(patientId, "ECG_ALERT_LOW", timestamp);
        }
        return null;
    }

}