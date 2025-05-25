package com.alerts.strategy;

import com.alerts.factory.BloodPressureAlertFactory;
import com.data_management.Patient;
import com.alerts.Alert;
import com.data_management.PatientRecord;

import java.util.List;

public class BloodPressureStrategy implements AlertStrategy{
    final int threshold = 10;
    final int systolicThreshold = 180;
    final int diastolicThreshold = 120;
    final int systolicBelow = 90;
    final int diastolicBelow = 60;

    public Alert trend(Patient patient, String condition, long timestamp) {
        BloodPressureAlertFactory factory = new BloodPressureAlertFactory();
        String patientId = String.valueOf(patient.getPatientId());

        List<PatientRecord> record = patient.getRecords(0, Integer.MAX_VALUE, condition);
        for (int i = 1; i < record.size(); i++) {
            if(record.get(i).getMeasurementValue() - record.get(i-1).getMeasurementValue() <= -threshold) {
                return factory.createAlert(patientId, "Increase in patient's" + condition, timestamp);
            }
            else if(record.get(i).getMeasurementValue() - record.get(i-1).getMeasurementValue() >= threshold) {
                return factory.createAlert(patientId, "Decrease in patient's" + condition, timestamp);
            }

        }
        return null;
    }

    public Alert checkAlert(Patient patient, String condition, long timestamp) {


        BloodPressureAlertFactory factory = new BloodPressureAlertFactory();

        String patientId = String.valueOf(patient.getPatientId());
        Alert alertSysTrend = trend(patient, "SystolicPressure", timestamp);
        Alert alertDiaTrend = trend(patient, "DiastolicPressure", timestamp);

        if (alertSysTrend != null) {
            return alertSysTrend;
        }
        if (alertDiaTrend != null) {
            return alertDiaTrend;
        }

        List<PatientRecord> record1 = patient.getRecords(0, Integer.MAX_VALUE, "SystolicPressure");
        List<PatientRecord> record2 = patient.getRecords(0, Integer.MAX_VALUE, "DiastolicPressure");

        if(record1.isEmpty() && record2.isEmpty()){
            return null;
        }

        if (record1.get(record1.size()-1).getRecordType().equals("SystolicPressure")) {
            if (record1.get(record1.size()-1).getMeasurementValue() < systolicBelow) {
                factory.createAlert(patientId, "SYSTOLIC PRESSURE TOO LOW", timestamp);
            } else if (record1.get(record1.size()-1).getMeasurementValue() > systolicThreshold) {
                factory.createAlert(patientId, "SYSTOLIC PRESSURE TOO HIGH", timestamp);
            }
        }
        else if (record1.get(record1.size()-1).getRecordType().equals("DiastolicPressure")) {
            if (record2.get(record1.size()-1).getMeasurementValue() < diastolicBelow) {
                factory.createAlert(patientId, "DIASTOLIC PRESSURE TOO LOW", timestamp);
            }
            else if (record2.get(record1.size()-1).getMeasurementValue() > diastolicThreshold) {
                factory.createAlert(patientId, "DIASTOLIC PRESSURE TOO HIGH", timestamp);
            }
        }

        return null;
    }

}
