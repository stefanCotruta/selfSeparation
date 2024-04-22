package ro.aero.service;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ro.aero.exception.EmergencyException;
import ro.aero.model.airport.Airport;
import ro.aero.model.Position;
import ro.aero.model.aircraft.Aircraft;
import ro.aero.model.aircraft.AircraftRepository;
import ro.aero.model.airport.AirportRepository;
import ro.aero.service.dto.AircraftDto;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class PriorityService {
    private final static Map<String, Double> WEIGHTS = Map.of(
            "distance", 0.4,
            "category", 0.2,
            "fuel", 0.3,
            "speed", 0.1
    );
    private final static double VERTICAL_DISTANCE = 3050;
    private final static double HORIZONTAL_DISTANCE = 20000;

    private AircraftRepository aircraftRepository;
    private AirportRepository airportRepository;
    private ModelMapper modelMapper;

    @PostConstruct
    public void initDb(){
        this.aircraftRepository.initDatabase();
        this.airportRepository.initDatabase();
    }

    public List<AircraftDto> prioritize(final String airportId) {
        Airport airport = this.airportRepository.findById(airportId);

        if (this.checkIfRunwayIsClosed(airportId)){
            throw new EmergencyException("RUNWAY IS CLOSED");
        }

        if (!getAircraftsWithEmergency().isEmpty()) {
            throw new EmergencyException("EMERGENCY FOR AIRCRAFT: %s ".formatted(
                            this.getAircraftsWithEmergency().stream()
                                    .map(aircraft -> this.mapAircraftToDto(aircraft, airport))
                                    .toList()));
        }

        return createPriorityMap(airport).entrySet()
                .stream()
                .sorted(Comparator.comparingDouble(Map.Entry<Aircraft, Double>::getValue))
                .map(Map.Entry::getKey)
                .filter(aircraft ->
                        aircraft.getPosition().z() < VERTICAL_DISTANCE &&
                        calculateDistance2D(aircraft.getPosition(), airport.getPosition()) < HORIZONTAL_DISTANCE)
                .map(aircraft -> this.mapAircraftToDto(aircraft, airport))
                .toList();
    }

    public void setEmergencyStatus(final String aircraftId,
                                   final Boolean emergencyStatus) {
        Aircraft aircraft = this.aircraftRepository.findAircraftById(aircraftId);
        aircraft.setEmergencyStatus(emergencyStatus);
        if (aircraft.getEmergencyStatus()) {
            log.info("EMERGENCY FOR AIRCRAFT: %s".formatted(aircraftId));
        } else {
            log.info("EMERGENCY RESOLVED FOR AIRCRAFT: %s".formatted(aircraftId));
        }
        this.aircraftRepository.save(aircraft);
    }

    public void setRunwayStatus(final String airportId,
                                final Boolean runwayStatus) {
        Airport airport = this.airportRepository.findById(airportId);
        airport.setIsRunwayClosed(runwayStatus);
        if (airport.getIsRunwayClosed()) {
            log.info("RUNWAY CLOSED FOR AIRPORT: %s".formatted(airportId));
        } else {
            log.info("RUNWAY OPEN FOR AIRPORT: %s".formatted(airportId));
        }
        this.airportRepository.save(airport);
    }

    private Map<Aircraft, Double> createPriorityMap(final Airport airport) {
        Map<Aircraft, Double> priorityMap = new HashMap<>();
        for (Aircraft aircraft : this.aircraftRepository.getAllAircrafts()){
            final Double distance = calculateDistance3D(aircraft.getPosition(), airport.getPosition());

            priorityMap.put(aircraft, calculatePriorityRating(
                    distance,
                    aircraft.getOrdinalCategory(),
                    aircraft.getFuelPercentage(),
                    aircraft.getSpeed()));
        }
        return priorityMap;
    }

    private Double calculateDistance3D(final Position aircraftPosition,
                                       final Position airportPosition) {
        return Math.sqrt(
                    Math.pow(aircraftPosition.x() - airportPosition.x(), 2) +
                    Math.pow(aircraftPosition.y() - airportPosition.y(), 2) +
                    Math.pow(aircraftPosition.z() - airportPosition.z(), 2));
    }

    private Double calculateDistance2D(final Position aircraftPosition,
                                       final Position airportPosition) {
        return Math.sqrt(
                    Math.pow(aircraftPosition.x() - airportPosition.x(), 2) +
                    Math.pow(aircraftPosition.y() - airportPosition.y(), 2));
    }

    private Double calculatePriorityRating(final Double distance,
                                           final Integer category,
                                           final Double fuel,
                                           final Integer speed) {
        return distance * WEIGHTS.get("distance") +
                category * WEIGHTS.get("category") +
                (1 - fuel) * WEIGHTS.get("fuel") +
                speed * WEIGHTS.get("speed");
    }

    private boolean checkIfRunwayIsClosed(final String airportId) {
        return this.airportRepository.findById(airportId).getIsRunwayClosed().equals(true);
    }

    private List<Aircraft> getAircraftsWithEmergency() {
        return this.aircraftRepository.getAllAircrafts().stream().filter(aircraft -> aircraft.getEmergencyStatus().equals(true)).toList();
    }

    private AircraftDto mapAircraftToDto(final Aircraft aircraft,
                                         final Airport airport) {
        AircraftDto dto = modelMapper.map(aircraft, AircraftDto.class);
        dto.setDistanceToAirport(
                calculateDistance3D(aircraft.getPosition(), airport.getPosition())
        );
        return dto;
    }
}