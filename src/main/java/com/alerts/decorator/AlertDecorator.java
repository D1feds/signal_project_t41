package com.alerts.decorator;

import com.alerts.Alert;

public class AlertDecorator extends Alert {
    public Alert alert;
    public int priority;

    public AlertDecorator(Alert alert) {
        super(alert.getPatientId(), alert.getCondition(), alert.getTimestamp());
        this.alert = alert;
    }

    @Override
    public String getPatientId() {
        return alert.getPatientId();
    }
    @Override
    public String getCondition() {
        return alert.getCondition();
    }
    @Override
    public long getTimestamp() {
        return alert.getTimestamp();
    }

}
