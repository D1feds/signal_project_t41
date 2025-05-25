package com.alerts.decorator;


import com.alerts.Alert;

public class RepeatedAlertDecorator extends AlertDecorator {

    private long nextRepeatTimestamp;

    public RepeatedAlertDecorator(Alert alert, long interval) {
        super(alert);
        this.nextRepeatTimestamp = calculateNextTimestamp(getTimestamp(), interval);
    }

    private long calculateNextTimestamp(long timestamp, long interval) {
        return timestamp + interval;
    }


    public void repeatAlert(long timestamp, long interval) {
        while (timestamp - interval > -getTimestamp()) {
            timestamp += interval;
        }
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