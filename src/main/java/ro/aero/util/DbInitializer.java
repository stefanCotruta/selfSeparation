package ro.aero.util;

import com.google.gson.Gson;
import ro.aero.model.airport.Airport;
import ro.aero.model.aircraft.Aircraft;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class DbInitializer {
    public static final String AIRCRAFT_FILE_PATH = "src/main/resources/aircrafts.json";
    public static final String AIRPORT_FILE_PATH = "src/main/resources/airports.json";

    public static Object[] initializeFromFile(final String path) {
            try (FileReader reader = new FileReader(path)) {
                Gson gson = new Gson();
                return switch (path) {
                    case AIRCRAFT_FILE_PATH -> gson.fromJson(reader, Aircraft[].class);
                    case AIRPORT_FILE_PATH -> gson.fromJson(reader, Airport[].class);

                    default -> throw new FileNotFoundException("File not found: " + path);
                };
            } catch (IOException e) {
                e.printStackTrace();
            }

            throw new RuntimeException("Database could not be initialized");
    }
}