package com.alerts;

import com.alerts.strategy.AlertStrategy;
import com.alerts.strategy.BloodOxygenStrategy;
import com.alerts.strategy.BloodPressureStrategy;
import com.alerts.strategy.ECGAlertStrategy;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private DataStorage dataStorage;
    public ArrayList<Alert> triggeredAlerts;
    public ArrayList<AlertStrategy> alertStrategies;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        this.triggeredAlerts = new ArrayList<>();
        this.alertStrategies = new ArrayList<>();
        init(this.dataStorage);

    }

    public void init(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        this.alertStrategies.add(new BloodPressureStrategy());
        this.alertStrategies.add(new BloodOxygenStrategy());
        this.alertStrategies.add(new ECGAlertStrategy());
    }
    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) throws IOException {
        for(AlertStrategy strategy: alertStrategies){
            List<PatientRecord> records = patient.getPatientRecords();
            for(int i =0; i< patient.getPatientRecords().size(); i++ ){
                PatientRecord record = records.get(i);
                Alert alert = strategy.checkAlert(patient, record.getRecordType(), record.getTimestamp());
                if(alert != null){ triggerAlert(alert);}
            }
        }
    }
    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    public void triggerAlert(Alert alert) {
        System.out.println("Patient  " + alert.getPatientId() +" has the following alert:  " +alert.getCondition() + ". Time:  " + alert.getTimestamp());
        triggeredAlerts.add(alert);
    }
}
