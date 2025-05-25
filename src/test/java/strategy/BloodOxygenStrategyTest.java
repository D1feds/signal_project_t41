package strategy;

import com.alerts.Alert;
import com.alerts.strategy.BloodOxygenStrategy;
import com.data_management.Patient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;

class BloodOxygenStrategyTest {




    private BloodOxygenStrategy bloodOxygenStrategy;
    private Patient patient;
    private static final String PATIENT_ID = "123";
    private static final int PATIENT_ID_INT = 123;
    private static final String CONDITION = "Saturation";
    private static final long TIMESTAMP = System.currentTimeMillis();
    private static final int VALUE = 80;

    @BeforeEach
    void setUp() {
        patient = new Patient(PATIENT_ID_INT);
        bloodOxygenStrategy = new BloodOxygenStrategy();
    }

    @Test
    void testAlertWhenBloodOxygenBelowThreshold() {
        patient.addRecord(VALUE, CONDITION, TIMESTAMP);

        Alert alert = bloodOxygenStrategy.checkAlert(patient, CONDITION, TIMESTAMP);

         assertNotNull(alert);
         assertEquals(PATIENT_ID, alert.getPatientId());
         assertEquals("Blood Oxygen Level Low", alert.getCondition());
         assertEquals(TIMESTAMP, alert.getTimestamp());
    }

    @Test
    void testAlertWhenBloodOxygenNormal() {
        patient.addRecord(VALUE + 20, CONDITION, TIMESTAMP);


        Alert alert = bloodOxygenStrategy.checkAlert(patient, CONDITION, TIMESTAMP);

        assertNull(alert);   }

    @Test
    void testAlertWithNoRecords() {

        Alert alert = bloodOxygenStrategy.checkAlert(patient, CONDITION, TIMESTAMP);
        assertNull(alert);
    }
}
