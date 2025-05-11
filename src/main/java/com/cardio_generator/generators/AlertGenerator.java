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
                //added "final" to a constant
                // Changed variable name to camelCase
                final double lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
                // Changed variable name to camelCase
                double p = -Math.expm1(-lambda); // Probability of at least one alert in the period
                boolean alertTriggered = RANDOM_GENERATOR.nextDouble() < p;

                if (alertTriggered) {
                    // Changed variable name to camelCase
                    this.alertStates[patientId] = true;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
}
