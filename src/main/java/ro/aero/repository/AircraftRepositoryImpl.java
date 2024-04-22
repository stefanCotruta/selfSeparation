package ro.aero.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import ro.aero.model.aircraft.Aircraft;
import ro.aero.model.aircraft.AircraftRepository;
import ro.aero.util.DbInitializer;


import java.util.Arrays;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Repository
public class AircraftRepositoryImpl implements AircraftRepository {
    private final MongoTemplate mongoTemplate;
    private final ModelMapper modelMapper;
    @Override
    public void save(Aircraft aircraft) {
        this.mongoTemplate.save(aircraft);
        log.info("Aircraft: %s saved to db".formatted(aircraft.toString()));
    }

    @Override
    public List<Aircraft> getAllAircrafts() {
        return this.mongoTemplate.findAll(Aircraft.class);
    }

    @Override
    public Aircraft findAircraftById(String aircraftId) {
        return this.mongoTemplate.findById(aircraftId, Aircraft.class);
    }

    @Override
    public void initDatabase() {
        if (this.getAllAircrafts().isEmpty()){
            Arrays.stream(DbInitializer.initializeFromFile(DbInitializer.AIRCRAFT_FILE_PATH))
                    .map(o -> this.modelMapper.map(o, Aircraft.class))
                    .forEach(aircraft -> {
                        aircraft.setEmergencyStatus(false);
                        this.save(aircraft);
                    });
            log.info("AIRCRAFT DATABASE INITIALIZED");
        }
    }
}