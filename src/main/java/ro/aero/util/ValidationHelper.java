package ro.aero.util;

import ro.aero.exception.ValidationException;

public class ValidationHelper {
    public static void validateAircraftParameters(final Double fuelPercentahe,
                                                  final Integer speed) {
        if (fuelPercentahe < 0 || fuelPercentahe > 1){
            throw new ValidationException("Invalid fuel percentage");
        }

        if (speed < 0 || speed > 2000){
            throw new ValidationException("Invalid speed");
        }
    }

    public static void validatePosition(final double x,
                                        final double y,
                                        final double z) {
        if (z < 0 || z > 100000 || x > 100000 || y > 100000){
            throw new ValidationException("Invalid position");
        }
    }
}
