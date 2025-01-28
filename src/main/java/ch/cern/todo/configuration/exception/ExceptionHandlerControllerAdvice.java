package ch.cern.todo.configuration.exception;

import ch.cern.todo.model.api.ErrorResponseApi;
import ch.cern.todo.model.business.CernException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CernException.class)
    public final ResponseEntity<ErrorResponseApi> cernExceptionHandler(CernException exception) {
        log.error("A CernException occurred", exception);
        val body = new ErrorResponseApi(exception.getMessage());
        return ResponseEntity.status(HttpStatus.valueOf(exception.getStatusCode()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(body);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponseApi> exceptionHandler(Exception exception) {
        log.error("An exception occurred", exception);
        val body = new ErrorResponseApi("Internal server error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body(body);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {
        val body = new HashMap<String, List<String>>();

        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(fieldError -> "field '" + fieldError.getField() + "' " + fieldError.getDefaultMessage())
            .toList();

        body.put("errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(body);
    }
}
