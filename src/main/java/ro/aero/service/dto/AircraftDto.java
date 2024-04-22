package ro.aero.service.dto;

import lombok.*;
import ro.aero.model.aircraft.Category;

@Data
public class AircraftDto {
    private String id;
    private String name;
    private Double distanceToAirport;
    private Category category;
    private Double fuelPercentage;
    private Integer speed;
}
