package com.cardio_generator.outputs;

/**
 * The {@code OutputStrategy} class is responsible for output of patient data,
 * using {@code output}.
 */
public interface OutputStrategy {
    /**
     * Function outputs data for certain patient.
     *
     * @param patientId the patient ID.
     * @param timestamp the timestamp of the record.
     * @param label the label of the data.
     * @param data the patient data.
     */
    void output(int patientId, long timestamp, String label, String data);
}
