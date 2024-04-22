package ro.aero.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ResponseException extends RuntimeException{
    private HttpStatus httpStatus;
    private String message;
}
