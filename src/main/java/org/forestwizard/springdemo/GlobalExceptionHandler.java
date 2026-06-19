package org.forestwizard.springdemo;

import org.apache.coyote.BadRequestException;
import org.forestwizard.springdemo.response.ErrorMessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorMessageResponse> handle(ResponseStatusException ex) {
        return generateResponse(HttpStatus.resolve(ex.getStatusCode().value()), ex.getLocalizedMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorMessageResponse> handle(UsernameNotFoundException ex) {
        return generateResponse(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorMessageResponse> handle(BadRequestException ex) {
        return generateResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorMessageResponse> handle(MissingServletRequestParameterException ex) {
        return generateResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
    }

    private ResponseEntity<ErrorMessageResponse> generateResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ErrorMessageResponse(message));
    }

}
