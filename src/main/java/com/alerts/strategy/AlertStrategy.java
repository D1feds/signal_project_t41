package com.alerts.strategy;

import com.alerts.Alert;
import com.data_management.Patient;

public interface AlertStrategy {
    Alert alert(Patient patient, String condition, long timestamp);

}
