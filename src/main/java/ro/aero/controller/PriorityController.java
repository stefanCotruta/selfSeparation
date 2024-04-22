package ro.aero.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.aero.service.PriorityService;
import ro.aero.service.dto.AircraftDto;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
public class PriorityController {
    private PriorityService priorityService;

    @GetMapping("priorities/{airportId}")
    public ResponseEntity<List<AircraftDto>> getPriority(final @PathVariable String airportId) {
        return ResponseEntity.ok(this.priorityService.prioritize(airportId));
    }

    @PostMapping("emergencies/aircraft/{aircraftId}")
    public ResponseEntity<Void> sendAircraftEmergency(final @PathVariable String aircraftId,
                                                      final @RequestBody boolean emergencyStatus) {
        this.priorityService.setEmergencyStatus(aircraftId, emergencyStatus);
        return ResponseEntity.ok().build();
    }

    @PostMapping("emergencies/airport/{airportId}")
    public ResponseEntity<Void> sendAirportEmergency(final @PathVariable String airportId,
                                                     final @RequestBody boolean runwayStatus) {
        this.priorityService.setRunwayStatus(airportId, runwayStatus);
        return ResponseEntity.ok().build();
    }
}