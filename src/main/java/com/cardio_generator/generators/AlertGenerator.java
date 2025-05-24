package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;
import java.util.Random;



public class AlertGenerator implements PatientDataGenerator {

    //changed the name of an object to ALL_CAPS_SNAKE_CASE
    public static final Random RANDOM_GENERATOR = new Random();

    // Changed variable name to camelCase
    private boolean[] alertStates; // false = resolved, true = pressed

    /**
     * Function initializes alert states array.
     * @param patientCount corresponds number of observed patients
     */
    public AlertGenerator(int patientCount) {
        //added "this" to variable(optional)
        // Changed variable name to camelCase
        this.alertStates = new boolean[patientCount + 1];
    }

    /**
     * Function outputs possible alerts due to its probability
     * @param patientId corresponds to the patientID
     * @param outputStrategy corresponds to used output strategy
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            long timestamp = System.currentTimeMillis();
            // Changed variable name to camelCase
            if (this.alertStates[patientId]) {
                //Magic number suppose to be in a constant
                final double resolveChance = 0.9;
                if (RANDOM_GENERATOR.nextDouble() < resolveChance) { // 90% chance to resolve
                    // Changed variable name to camelCase
                    this.alertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert Resolved", "Alert resolved for " + patientId);
                }
            } else {
                // changed/commented for the new logic of alert


                //added "final" to a constant
                // Changed variable name to camelCase
//                final double lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
//                // Changed variable name to camelCase
//                double p = -Math.expm1(-lambda); // Probability of at least one alert in the period
//                boolean alertTriggered = RANDOM_GENERATOR.nextDouble() < p;


                int[] systolicReadings = getSystolicDocument(patientId);
                int[] diastolicReadings = getDiastolicDocument(patientId);
                // trend data check
                if (isTrendAlert(systolicReadings) || isTrendAlert(diastolicReadings)) {
                    triggerAlert(patientId, "Trend Alert", outputStrategy, timestamp);
                }
                // in case where records does not seem to be alerted according to the trend need to check the Critical values for BP Data(systolic and diastolic)
                if (isCriticalAlert(systolicReadings, diastolicReadings)) {
                    triggerAlert(patientId, "Critical Alert", outputStrategy, timestamp);
                }

            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
    private void triggerAlert(int patientId, String alertType, OutputStrategy outputStrategy, long timestamp) {
        alertStates[patientId] = true;
        outputStrategy.output(patientId, timestamp, alertType, "Patient: " + patientId + " has " + alertType + ", need help");
    }

    private boolean isTrendAlert(int[] data) {
        if (data.length < 3) return false;
        for (int i = 2; i < data.length; i++) {
            if (Math.abs(data[i] - data[i - 1]) > 10 && Math.abs(data[i - 1] - data[i - 2]) > 10) {
                if ((data[i] > data[i - 1] && data[i - 1] > data[i - 2]) ||
                        (data[i] < data[i - 1] && data[i - 1] < data[i - 2])) {
                    return true;
                }
            }
        }
        return false;
    }
    private boolean isCriticalAlert(int[] systolicDocuments, int[] diastolicDocuments) {
        for (int systolicRecord : systolicDocuments) {
            if (systolicRecord > 180 || systolicRecord < 90)
                return true;
        }
        for (int diastolicRecord : diastolicDocuments) {
            if (diastolicRecord > 120 || diastolicRecord < 60)
                return true;
        }
        return false;
    }

    private int[] getSystolicDocument(int patientId){
        //logic for read the document
        //i'll use int[] for test
        //read(patientid, timesample)
        //return document
        return new int[]{110, 120, 130, 140};
    }
    //same for diastolic
    private int[] getDiastolicDocument(int patientId){
        return new int[]{80, 85, 90, 95};
    }
}
