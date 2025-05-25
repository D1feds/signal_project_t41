package strategy;

import com.alerts.Alert;
import com.alerts.strategy.ECGAlertStrategy;
import com.data_management.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ECGAlertStrategyTest {


    private ECGAlertStrategy ecgAlertStrategy;
    private Patient patient;
    private static final String PATIENT_ID = "123";
    private static final int PATIENT_ID_INT = 123;
    private static final String CONDITION = "ECG";
    private static final long TIMESTAMP = System.currentTimeMillis();
    private static final int THRESHOLD = 100;

    @BeforeEach
    void setUp() {
        this.patient = new Patient(PATIENT_ID_INT);
        ecgAlertStrategy = new ECGAlertStrategy();
    }

    @Test
    void testAlertWhenECGValueAboveThreshold() {
        this.patient.addRecord(THRESHOLD + 50, CONDITION, TIMESTAMP);
        System.out.println(this.patient.getPatientRecords().size());
        Alert alert = ecgAlertStrategy.checkAlert(this.patient, CONDITION, TIMESTAMP);


         assertNotNull(alert);
         assertEquals(PATIENT_ID, alert.getPatientId());
         assertEquals("ECG_ALERT_HIGH", alert.getCondition());
         assertEquals(TIMESTAMP, alert.getTimestamp());
    }

    @Test
    void testAlertWhenECGValueBelowThreshold() {

        patient.addRecord(THRESHOLD - 60, CONDITION, TIMESTAMP);


        Alert alert = ecgAlertStrategy.checkAlert(patient, CONDITION, TIMESTAMP);

         assertNotNull(alert);
         assertEquals(PATIENT_ID, alert.getPatientId());
         assertEquals("ECG_ALERT_LOW", alert.getCondition());
         assertEquals(TIMESTAMP, alert.getTimestamp());
    }


    //alert method with no records

    @Test
    void testAlertWithNoRecords() {
        Alert alert = ecgAlertStrategy.checkAlert(patient, CONDITION, TIMESTAMP);
        assertNull(alert);
    }
}
