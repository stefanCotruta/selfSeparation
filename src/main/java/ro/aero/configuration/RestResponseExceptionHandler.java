package ro.aero.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ro.aero.exception.ResponseException;

@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { ResponseException.class })
    protected ResponseEntity<Object> handleException(final ResponseException exception) {
        final var body = new ExceptionBody(exception.getMessage());
        return new ResponseEntity<>(body, exception.getHttpStatus());
    }

    @Data
    @AllArgsConstructor
    static class ExceptionBody {
        private String message;
    }
}
