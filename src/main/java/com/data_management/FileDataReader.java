package com.data_management;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.crypto.Data;

/**
 * Reads data from files in a specified directory and stores it in the data storage.
 * This class implements the {@link DataReader} interface to provide file-based data reading functionality.
 */
public class FileDataReader implements DataReader {

    private String outputDirectory;

    /**
     * Constructs a FileDataReader with the specified output directory.
     *
     * @param outputDirectory the directory where the files are located
     */
    public FileDataReader(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    /**
     * Reads data from the files in the specified output directory and stores it in the data storage.
     *
     * @param dataStorage the storage where data will be stored
     * @throws IOException if there is an error reading the data
     */
    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        File directory = new File(outputDirectory);

        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("The specified output directory does not exist or is not a directory.");
        }

        DataStorage storage = DataStorage.getInstance();
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    parseFile(file, storage);
                }
            }
        }
    }

    /**
     * Parses a single file and stores the data in the data storage.
     *
     * @param file the file to be parsed
     * @param dataStorage the storage where data will be stored
     */
    private void parseFile(File file, DataStorage dataStorage) {
        DataStorage storage = DataStorage.getInstance();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    int patientId = Integer.parseInt(parts[0].split(" ")[2]);
                    long timestamp = Long.parseLong(parts[1].split(" ")[2]);
                    String recordType = parts[2].split(" ")[2];
                    double measurementValue = Double.parseDouble(parts[3].split(" ")[2].replace("%", ""));

                    // Add the parsed data to the data storage
                    storage.addPatientData(patientId, measurementValue, recordType, timestamp);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        //no need in file read
    }

    @Override
    public void stop() {
        //no need in file read
    }
}