package com.alerts.factory;

import com.alerts.Alert;

public class ECGAlertFactory extends AlertFactory{

    public static final String ECG_ALERT = "ECG";


    public Alert createAlert(String patientId, String condition, long timestamp) {
            return super.createAlert(patientId, condition, timestamp);
        }
    }

