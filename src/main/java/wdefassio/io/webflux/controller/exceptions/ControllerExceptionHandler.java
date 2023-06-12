package wdefassio.io.webflux.controller.exceptions;


import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;
import wdefassio.io.webflux.service.exception.ObjectNotFoundException;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DuplicateKeyException.class)
    ResponseEntity<Mono<StandardError>> duplicatedKey(DuplicateKeyException ex, ServerHttpRequest request) {
        return ResponseEntity.badRequest().body(Mono.just(StandardError.builder()
                .timestamp(LocalDateTime.now())
                .status(BAD_REQUEST.value())
                .error(BAD_REQUEST.getReasonPhrase())
                .message(verifyDupKey(ex.getMessage()))
                .path(request.getPath().toString())
                .build()));
    }
    @ExceptionHandler(ObjectNotFoundException.class)
    ResponseEntity<Mono<StandardError>> objectNotFound(ObjectNotFoundException ex, ServerHttpRequest request) {
        return ResponseEntity.status(NOT_FOUND).body(Mono.just(StandardError.builder()
                .timestamp(LocalDateTime.now())
                .status(NOT_FOUND.value())
                .error(NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getPath().toString())
                .build()));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    ResponseEntity<Mono<ValidationError>> validationError(WebExchangeBindException ex, ServerHttpRequest request) {

        ValidationError validationError = new ValidationError(
                LocalDateTime.now(),
                request.getPath().toString(),
                BAD_REQUEST.value(),
                "Validation error",
                "Error on attributes");

        for(FieldError error: ex.getFieldErrors()) {
             validationError.addError(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(BAD_REQUEST).body(Mono.just(validationError));
    }




    private String verifyDupKey(String message) {
        if (message.contains("email dup key")) {
            return "Email already exists!";
        } else
            return "Dup key exception";
    }
}
