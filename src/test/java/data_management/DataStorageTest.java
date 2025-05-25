package data_management;

import static org.junit.jupiter.api.Assertions.*;

import com.data_management.DataReader;
import com.data_management.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;

import java.io.IOException;
import java.util.List;

class DataStorageTest {
        private DataStorage storage;

        @BeforeEach
        public void setup() {
            storage = new DataStorage();
        }

        @Test
        public void testGetAllPatientsInitiallyEmpty() {
            List<Patient> patients = storage.getAllPatients();
            assertNotNull(patients, "getAllPatients should never return null");
            assertTrue(patients.isEmpty(), "New storage should contain no patients");
        }

        @Test
        public void testAddPatientDataCreatesNewPatient() {
            storage.addPatientData(1, 72.5, "HeartRate", 1000L);
            Patient p = storage.getPatient(1);
            assertNotNull(p, "After adding data, patient should exist");
            List<Patient> patients = storage.getAllPatients();
            assertEquals(1, patients.size(), "There should be exactly one patient");
        }

        @Test
        public void testAddPatientDataRecordsSamePatient() {
            storage.addPatientData(1, 72.5, "HeartRate", 1000L);
            storage.addPatientData(1, 80.0, "BloodPressure", 2000L);
            List<PatientRecord> records = storage.getPatientRecords(1);
            assertEquals(2, records.size(), "Patient should have two records");
            assertTrue(records.stream().anyMatch(r -> r.getRecordType().equals("HeartRate") && r.getMeasurementValue() == 72.5 && r.getTimestamp() == 1000L));
            assertTrue(records.stream().anyMatch(r -> r.getRecordType().equals("BloodPressure") && r.getMeasurementValue() == 80.0 && r.getTimestamp() == 2000L));
        }

        @Test
        public void testGetRecordsForNonexistentPatient() {
            List<PatientRecord> recs = storage.getRecords(999, 0L, 10000L, "OxygenSaturation");
            assertNotNull(recs, "Should return empty list, not null");
            assertTrue(recs.isEmpty(), "No records for nonexistent patient");
        }

        @Test
        public void testGetPatientReturnsNullIfAbsent() {
            assertNull(storage.getPatient(42), "getPatient should return null for missing ID");
        }

        @Test
        public void testGetPatientRecordsReturnsCorrectList() {
            storage.addPatientData(2, 55.5, "OxygenSaturation", 500L);
            storage.addPatientData(2, 60.0, "OxygenSaturation", 1000L);
            List<PatientRecord> recs = storage.getPatientRecords(2);
            assertEquals(2, recs.size(), "getPatientRecords should return all records for the patient");
        }
    }
