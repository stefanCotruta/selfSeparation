package ro.aero.model.aircraft;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ro.aero.util.ValidationHelper;
import ro.aero.model.Position;

@ToString
@Getter
public class Aircraft {
    private String id;
    private final String name;
    private final Position position;
    private final Category category;
    private final Double fuelPercentage;
    private final Integer speed;
    @Setter
    private Boolean emergencyStatus = false;

    public Aircraft(final String name,
                    final Position position,
                    final Category category,
                    final Double fuelPercentage,
                    final Integer speed) {

        ValidationHelper.validateAircraftParameters(fuelPercentage, speed);

        this.name = name;
        this.position = position;
        this.category = category;
        this.fuelPercentage = fuelPercentage;
        this.speed = speed;
    }

    public Integer getOrdinalCategory(){
        return this.category.ordinal() + 1;
    }
}
