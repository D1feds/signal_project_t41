package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * The {@code PatientDataGenerator} interface class is responsible for generating record for certain patient,
 * using {@code generate}.
 */
public interface PatientDataGenerator {
    /**
     * Function generates data for a certain patient.
     *
     * @param patientId the patient ID.
     * @param outputStrategy is the chosen output strategy.
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
