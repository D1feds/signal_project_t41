package strategy;

import com.alerts.Alert;
import com.alerts.strategy.BloodPressureStrategy;
import com.data_management.Patient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;

class BloodPressureStrategyTest {

    private BloodPressureStrategy bloodPressureStrategy;
    private Patient patient;
    private static final String PATIENT_ID = "123";
    private static final int PATIENT_ID_INT = 123;
    private static final String SYSTOLIC_CONDITION = "SystolicPressure";
    private static final String DIASTOLIC_CONDITION = "DiastolicPressure";
    private final long TIMESTAMP = System.currentTimeMillis();
    private static final int SYSTOLIC_THRESHOLD_HIGH = 180;
    private static final int SYSTOLIC_THRESHOLD_LOW = 90;
    private static final int DIASTOLIC_THRESHOLD_HIGH = 120;
    private static final int DIASTOLIC_THRESHOLD_LOW = 60;
    private static final int TREND_THRESHOLD = 10;

    @BeforeEach
    void setUp() {
        patient = new Patient(PATIENT_ID_INT);
        bloodPressureStrategy = new BloodPressureStrategy();
    }

    @Test
    void testTrendWithIncreasingPressure() {

        patient.addRecord(100, SYSTOLIC_CONDITION, TIMESTAMP - 4000);
        patient.addRecord(110, SYSTOLIC_CONDITION, TIMESTAMP - 3000);
        patient.addRecord(120, SYSTOLIC_CONDITION, TIMESTAMP - 2000);
        patient.addRecord(140, SYSTOLIC_CONDITION, TIMESTAMP - 1000); // Increase of 20, above threshold


        Alert alert = bloodPressureStrategy.trend(patient, SYSTOLIC_CONDITION, TIMESTAMP);




         assertNotNull(alert);
         assertEquals(PATIENT_ID, alert.getPatientId());
         assertEquals("Increase in patient's blood pressure", alert.getCondition());
         assertEquals(TIMESTAMP, alert.getTimestamp());
    }

    @Test
    void testTrendWithDecreasingPressure() {

        patient.addRecord(140, SYSTOLIC_CONDITION, TIMESTAMP - 4000);
        patient.addRecord(130, SYSTOLIC_CONDITION, TIMESTAMP - 3000);
        patient.addRecord(120, SYSTOLIC_CONDITION, TIMESTAMP - 2000);
        patient.addRecord(100, SYSTOLIC_CONDITION, TIMESTAMP - 1000); // Decrease of 20, above threshold

        Alert alert = bloodPressureStrategy.trend(patient, SYSTOLIC_CONDITION, TIMESTAMP);

         assertNotNull(alert);
         assertEquals(PATIENT_ID, alert.getPatientId());
         assertEquals("Decrease in patient's blood pressure", alert.getCondition());
         assertEquals(TIMESTAMP, alert.getTimestamp());
    }

    @Test
    void testTrendWithStablePressure() {

        patient.addRecord(120, SYSTOLIC_CONDITION, TIMESTAMP - 4000);
        patient.addRecord(122, SYSTOLIC_CONDITION, TIMESTAMP - 3000);
        patient.addRecord(121, SYSTOLIC_CONDITION, TIMESTAMP - 2000);
        patient.addRecord(123, SYSTOLIC_CONDITION, TIMESTAMP - 1000); // Changes within threshold


        Alert alert = bloodPressureStrategy.trend(patient, SYSTOLIC_CONDITION, TIMESTAMP);

        assertNull(alert);
    }

    @Test
    void testAlertWithSystolicPressureTooHigh() {

        for (int i = 0; i < 5; i++) {
            patient.addRecord(SYSTOLIC_THRESHOLD_HIGH + 10, "BloodPressure", TIMESTAMP - i * 1000);
        }


        Alert alert = bloodPressureStrategy.alert(patient, SYSTOLIC_CONDITION, TIMESTAMP);

         assertNotNull(alert);
         assertEquals(PATIENT_ID, alert.getPatientId());
         assertEquals("SYSTOLIC PRESSURE TOO HIGH", alert.getCondition());
         assertEquals(TIMESTAMP, alert.getTimestamp());
    }

    @Test
    void testAlertWithSystolicPressureTooLow() {

        for (int i = 0; i < 5; i++) {
            patient.addRecord(SYSTOLIC_THRESHOLD_LOW - 10, "BloodPressure", TIMESTAMP - i * 1000);
        }


        Alert alert = bloodPressureStrategy.alert(patient, SYSTOLIC_CONDITION, TIMESTAMP);

         assertNotNull(alert);
         assertEquals(PATIENT_ID, alert.getPatientId());
         assertEquals("SYSTOLIC PRESSURE TOO LOW", alert.getCondition());
         assertEquals(TIMESTAMP, alert.getTimestamp());
    }

    @Test
    void testAlertWithDiastolicPressureTooHigh() {

        for (int i = 0; i < 5; i++) {
            patient.addRecord(DIASTOLIC_THRESHOLD_HIGH + 10, "BloodPressure", TIMESTAMP - i * 1000);
        }


        Alert alert = bloodPressureStrategy.alert(patient, DIASTOLIC_CONDITION, TIMESTAMP);

         assertNotNull(alert);
         assertEquals(PATIENT_ID, alert.getPatientId());
         assertEquals("DIASTOLIC PRESSURE TOO HIGH", alert.getCondition());
         assertEquals(TIMESTAMP, alert.getTimestamp());
    }

    @Test
    void testAlertWithDiastolicPressureTooLow() {

        for (int i = 0; i < 5; i++) {
            patient.addRecord(DIASTOLIC_THRESHOLD_LOW - 10, "BloodPressure", TIMESTAMP - i * 1000);
        }


        Alert alert = bloodPressureStrategy.alert(patient, DIASTOLIC_CONDITION, TIMESTAMP);

         assertNotNull(alert);
         assertEquals(PATIENT_ID, alert.getPatientId());
         assertEquals("DIASTOLIC PRESSURE TOO LOW", alert.getCondition());
         assertEquals(TIMESTAMP, alert.getTimestamp());
    }

    @Test
    void testAlertWithNormalPressure() {
        for (int i = 0; i < 5; i++) {
            patient.addRecord((SYSTOLIC_THRESHOLD_HIGH + SYSTOLIC_THRESHOLD_LOW) / 2, "BloodPressure", TIMESTAMP - i * 1000);
        }

        Alert alert = bloodPressureStrategy.alert(patient, SYSTOLIC_CONDITION, TIMESTAMP);

        assertNull(alert);
    }

    @Test
    void testAlertWithNoRecords() {

        Alert alert = bloodPressureStrategy.alert(patient, SYSTOLIC_CONDITION, TIMESTAMP);
        assertNull(alert);
    }
}


