package ua.vg.msg.userservice.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ua.vg.msg.userservice.service.exception.InvalidRefreshTokenException;
import ua.vg.msg.userservice.service.exception.UserAlreadyRegisteredException;
import ua.vg.msg.userservice.service.exception.UserNotFoundException;

import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * ExceptionHandler — TODO.
 *
 * @author ykalapusha
 * @since 08.08.2026
 */
@ControllerAdvice
@Slf4j
public class CommonExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    ProblemDetail handleUserNotFoundException(UserNotFoundException ex) {
        log.info("User Not Found exception raised");
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(NOT_FOUND, ex.getMessage());
        problemDetail.setType(java.net.URI.create("user-not-found"));
        problemDetail.setTitle("User Not Found");
        return problemDetail;
    }

    @ExceptionHandler(UserAlreadyRegisteredException.class)
    ProblemDetail handleUserAlreadyRegisteredException(UserAlreadyRegisteredException ex) {
        log.info("User Already Registered exception raised");
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(CONFLICT, ex.getMessage());
        problemDetail.setType(java.net.URI.create("user-already-registered"));
        problemDetail.setTitle("User Already Registered");
        return problemDetail;
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    ProblemDetail handleInvalidRefreshTokenException(InvalidRefreshTokenException ex) {
        log.info("Invalid Refresh Token exception raised");
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(UNAUTHORIZED, ex.getMessage());
        problemDetail.setType(java.net.URI.create("invalid-refresh-token"));
        problemDetail.setTitle("Invalid Refresh Token");
        return problemDetail;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, Object> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "Invalid value"
                ));

        log.info("Method Argument Not Valid exception raised");
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(BAD_REQUEST, "Validation failed");
        problemDetail.setType(java.net.URI.create("method-argument-not-valid"));
        problemDetail.setTitle("Method Argument Not Valid");
        problemDetail.setProperty("errors", errors);
        return ResponseEntity.badRequest().body(problemDetail);
    }
}
