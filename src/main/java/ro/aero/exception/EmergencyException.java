package ro.aero.exception;

import org.springframework.http.HttpStatus;

public class EmergencyException extends ResponseException {
    public EmergencyException(final String message) {
        super.setHttpStatus(HttpStatus.SERVICE_UNAVAILABLE);
        super.setMessage(message);
    }
}
