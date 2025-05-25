package com.alerts.factory;

import com.alerts.Alert;

public class BloodPressureAlertFactory extends AlertFactory {


    public Alert createAlert(String patientId, String condition, long timestamp) {

            return super.createAlert(patientId, condition, timestamp);


    }


}
