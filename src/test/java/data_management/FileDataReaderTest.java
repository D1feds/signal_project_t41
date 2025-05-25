package data_management;

import com.data_management.DataStorage;
import com.data_management.FileDataReader;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the FileDataReader class.
 */
public class FileDataReaderTest {
    @BeforeEach
    public void resetDataStorageSingleton() throws Exception {
        // Reset the singleton instance before each test
        Field instanceField = DataStorage.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);
    }

    @Test
    public void testReadDataThrowsOnInvalidDirectory() {
        String invalidDir = "nonexistent_dir_12345";
        FileDataReader reader = new FileDataReader(invalidDir);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> reader.readData(new DataStorage()));
        assertTrue(ex.getMessage().contains("does not exist or is not a directory"));
    }

    @Test
    public void testReadDataEmptyDirectory(@TempDir Path tempDir) throws IOException {
        // Create an empty directory
        FileDataReader reader = new FileDataReader(tempDir.toString());
        reader.readData(new DataStorage());

        DataStorage storage = DataStorage.getInstance();
        List<Patient> patients = storage.getAllPatients();
        assertNotNull(patients);
        assertTrue(patients.isEmpty(), "No patients should be read from an empty directory");
    }

//    @Test
//    public void testReadDataParsesValidFiles(@TempDir Path tempDir) throws IOException {
//        Path file1 = tempDir.resolve("data1.csv");
//        try (BufferedWriter writer = Files.newBufferedWriter(file1)) {
//            writer.write("1,1000,TypeA,10.5\n");
//            writer.write("2,2000,TypeB,20.0\n");
//            writer.write("invalid,line,too,few\n");
//        }
//        Path file2 = tempDir.resolve("data2.csv");
//        try (BufferedWriter writer = Files.newBufferedWriter(file2)) {
//            writer.write("1,1500,TypeA,15.0\n");
//        }
//
//        FileDataReader reader = new FileDataReader(tempDir.toString());
//        reader.readData(new DataStorage());
//
//        DataStorage storage = DataStorage.getInstance();
//        // We expect patient 1 with two records and patient 2 with one record
//        Patient patient1 = storage.getPatient(1);
//        assertNotNull(patient1, "Patient 1 should exist");
//        List<PatientRecord> records1 = storage.getPatientRecords(1);
//        assertEquals(2, records1.size(), "Patient1 should have exactly 2 valid records");
//        assertTrue(records1.stream().anyMatch(r -> r.getRecordType().equals("TypeA") && r.getMeasurementValue() == 10.5 && r.getTimestamp() == 1000L));
//        assertTrue(records1.stream().anyMatch(r -> r.getRecordType().equals("TypeA") && r.getMeasurementValue() == 15.0 && r.getTimestamp() == 1500L));
//
//        Patient patient2 = storage.getPatient(2);
//        assertNotNull(patient2, "Patient 2 should exist");
//        List<PatientRecord> records2 = storage.getPatientRecords(2);
//        assertEquals(1, records2.size(), "Patient2 should have exactly 1 valid record");
//        PatientRecord rec2 = records2.get(0);
//        assertEquals("TypeB", rec2.getRecordType());
//        assertEquals(20.0, rec2.getMeasurementValue());
//        assertEquals(2000L, rec2.getTimestamp());
//    }
}
