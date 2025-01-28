package ch.cern.todo.model.business;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

// TODO: Error messages should use a more structured approach, like using error codes (i.e. using enums) and support localization
public class CernException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 9103378622070575067L;

    @Getter
    private final int statusCode;

    public CernException(String message, HttpStatus httpStatus) {
        this(message, httpStatus.value());
    }

    public CernException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public CernException(String message, HttpStatus httpStatus, Throwable cause) {
        this(message, httpStatus.value(), cause);
    }

    public CernException(String message, int statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }
}
