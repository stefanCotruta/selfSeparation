package ro.aero.model;

import ro.aero.util.ValidationHelper;

public record Position(Double x, Double y, Double z) {
    public Position {
        ValidationHelper.validatePosition(x, y, z);
    }
}
