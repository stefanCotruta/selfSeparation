package ro.aero.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import ro.aero.model.airport.Airport;
import ro.aero.model.airport.AirportRepository;
import ro.aero.util.DbInitializer;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Repository
@AllArgsConstructor
public class AirportRepositoryImpl implements AirportRepository {
    private MongoTemplate mongoTemplate;
    private ModelMapper modelMapper;

    @Override
    public void save(Airport airport) {
        this.mongoTemplate.save(airport);
        log.info("Airport: %s saved to db".formatted(airport.toString()));
    }

    @Override
    public List<Airport> getAllAirports() {
        return this.mongoTemplate.findAll(Airport.class);
    }

    @Override
    public Airport findById(String id) {
        return this.mongoTemplate.findById(id, Airport.class);
    }

    @Override
    public void initDatabase() {
        if (this.getAllAirports().isEmpty()){
            Arrays.stream(DbInitializer.initializeFromFile(DbInitializer.AIRPORT_FILE_PATH))
                    .map(o -> this.modelMapper.map(o, Airport.class))
                    .forEach(airport -> {
                        airport.setIsRunwayClosed(false);
                        this.save(airport);
                    });
            log.info("AIRPORT DATABASE INITIALIZED");
        }
    }
}