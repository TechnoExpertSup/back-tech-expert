package ua.customer.controller.handler;

import jakarta.servlet.http.HttpServletRequest;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.customer.dto.response.ErrorResponse;
import ua.customer.error.CustomerAlreadyExistException;
import ua.customer.error.CustomerNotFoundException;

import java.util.Objects;
import java.util.stream.Collectors;


@RestControllerAdvice
@Slf4j
public class CustomerExceptionHandler {
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException e, HttpServletRequest request) {
        String message = e.getMessage();
        ErrorResponse errorResponse = new ErrorResponse(message, request.getRequestURI());
        log.error("There was an authentication error for the request URI : {} ", request.getRequestURI());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        String message = "You do not have sufficient rights to access this resource. Try logging in.";
        ErrorResponse errorResponse = new ErrorResponse(message, request.getRequestURI());
        log.error("An access denied error occurred for the request URI : {}", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception
            , HttpServletRequest request) {
        String errorMessage = Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage();

        ErrorResponse errorResponse = new ErrorResponse(errorMessage, request.getRequestURI());
        log.error("An validation body error occurred for the request URI : {} , \n message : {}"
                , request.getRequestURI(), errorMessage);
        return ResponseEntity.badRequest()
                .body(errorResponse);

    }

    @ExceptionHandler(CustomerAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handlerCustomerAlreadyExistException(CustomerAlreadyExistException exception
            , HttpServletRequest request) {
        log.error(" An customer already exist error occurred for the request URI : {} , \n message : {} "
                , request.getRequestURI(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(exception.getMessage(), request.getRequestURI()));

    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerCustomerNotFoundException(CustomerNotFoundException exception
            , HttpServletRequest request) {
        log.error(" An customer not found error occurred for the request URI : {} , \n message : {} "
                , request.getRequestURI(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(exception.getMessage(), request.getRequestURI()));

    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handlerConstraintViolationException(ConstraintViolationException exception
            , HttpServletRequest request) {
        String  error = exception.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath().toString())
                .collect(Collectors.joining("\n"));
        String errorMessage = " Invalid query parameters .";
        ErrorResponse errorResponse = new ErrorResponse(errorMessage, request.getRequestURI());
        log.error("An constraint violation error occurred for the request : {}, \n method.parameter : {} "
                , request.getRequestURI(), error);
        return ResponseEntity.badRequest()
                .body(errorResponse);

    }

}
