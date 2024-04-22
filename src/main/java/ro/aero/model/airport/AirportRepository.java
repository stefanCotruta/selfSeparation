package ro.aero.model.airport;

import java.util.List;

public interface AirportRepository {

    void save(final Airport airport);

    List<Airport> getAllAirports();

    Airport findById(final String id);

    void initDatabase();
}
