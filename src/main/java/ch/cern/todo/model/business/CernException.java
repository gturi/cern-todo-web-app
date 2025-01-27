package ch.cern.todo.model.business;

import lombok.Getter;
import org.springframework.http.HttpStatus;

// TODO: Error messages should use a more structured approach, like using error codes (i.e. using enums) and support localization
public class CernException extends RuntimeException {

    @Getter
    private final int statusCode;

    public CernException(String message, HttpStatus httpStatus) {
        super(message);
        this.statusCode = httpStatus.value();
    }

    public CernException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public CernException(String message, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
            this.statusCode = httpStatus.value();
    }
}
