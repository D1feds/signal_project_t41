package com.alerts.decorator;

import com.alerts.Alert;
import com.alerts.decorator.AlertDecorator;

public class PriorityAlertDecorator extends AlertDecorator{


    public PriorityAlertDecorator(Alert alert, int prioirity) {
        super(alert);
        super.priority = prioirity;
    }

    @Override
    public String getPatientId() {
        return super.getPatientId();
    }
    @Override
    public String getCondition() {
        return super.getCondition();
    }
    @Override
    public long getTimestamp() {
        return super.getTimestamp();
    }
}
