package ua.customer.controller.exceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ua.customer.dto.ErrorResponse;
import ua.customer.error.CustomerAlreadyExistException;
import ua.customer.error.CustomerNotFoundException;

import java.util.Objects;


@ControllerAdvice
public class CustomerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception
            , HttpServletRequest request) {
        String errorMessage = Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage();
        String requestUri = request.getRequestURL().toString();
        ErrorResponse errorResponse = new ErrorResponse(errorMessage, requestUri);

        return ResponseEntity.badRequest()
                .body(errorResponse);

    }
    @ExceptionHandler(CustomerAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handlerCustomerAlreadyExistException(CustomerAlreadyExistException exception
            , HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(exception.getMessage(), request.getRequestURL().toString()));

    }
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerCustomerNotFoundException(CustomerNotFoundException exception
            ,HttpServletRequest request){

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(exception.getMessage(),request.getRequestURL().toString() ));

    }


}
