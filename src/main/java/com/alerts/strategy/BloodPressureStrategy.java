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

        List<PatientRecord> record = patient.getRecords(0, Long.MAX_VALUE, condition);
        for (int i = 1; i < record.size(); i++) {
            String conditionMessage = condition;
            if(record.get(i).getMeasurementValue() - record.get(i-1).getMeasurementValue() >= threshold) {
                if (condition.equals("SystolicPressure")) {
                    conditionMessage = "Systolic Pressure";
                } else if (condition.equals("DiastolicPressure")) {
                    conditionMessage = "Diastolic Pressure";
                }
                return factory.createAlert(patientId, "Increase in patient's " + conditionMessage, timestamp);
            }
            else if(record.get(i).getMeasurementValue() - record.get(i-1).getMeasurementValue() <= -threshold) {
                if (condition.equals("SystolicPressure")) {
                    conditionMessage = "Systolic Pressure";
                } else if (condition.equals("DiastolicPressure")) {
                    conditionMessage = "Diastolic Pressure";
                }
                return factory.createAlert(patientId, "Decrease in patient's " + conditionMessage, timestamp);
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

        List<PatientRecord> record1 = patient.getRecords(0, Long.MAX_VALUE, "SystolicPressure");
        List<PatientRecord> record2 = patient.getRecords(0, Long.MAX_VALUE, "DiastolicPressure");

        if(record1.isEmpty() && record2.isEmpty()){
            return null;
        }


        if (condition.equals("SystolicPressure") && !record1.isEmpty()) {
            if (record1.get(record1.size()-1).getMeasurementValue() < systolicBelow) {
                return factory.createAlert(patientId, "SYSTOLIC PRESSURE TOO LOW", timestamp);
            } else if (record1.get(record1.size()-1).getMeasurementValue() > systolicThreshold) {
                return factory.createAlert(patientId, "SYSTOLIC PRESSURE TOO HIGH", timestamp);
            }
        }

        else if (condition.equals("DiastolicPressure") && !record2.isEmpty()) {
            if (record2.get(record2.size()-1).getMeasurementValue() < diastolicBelow) {
                return factory.createAlert(patientId, "DIASTOLIC PRESSURE TOO LOW", timestamp);
            }
            else if (record2.get(record2.size()-1).getMeasurementValue() > diastolicThreshold) {
                return factory.createAlert(patientId, "DIASTOLIC PRESSURE TOO HIGH", timestamp);
            }
        }

        return null;
    }

}
