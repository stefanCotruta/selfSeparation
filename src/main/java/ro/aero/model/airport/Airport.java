package ro.aero.model.airport;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ro.aero.model.Position;

@Getter
@ToString
public class Airport {
    private String id;
    private final String name;
    private final Position position;
    @Setter
    private Boolean isRunwayClosed = false;

    public Airport(final String name,
                   final Position position) {
        this.name = name;
        this.position = position;
    }
}