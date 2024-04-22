package ro.aero.model.aircraft;

import java.util.List;

public interface AircraftRepository {
    void save(final Aircraft aircraft);

    List<Aircraft> getAllAircrafts();

    Aircraft findAircraftById(final String aircraftId);

    void initDatabase();
}