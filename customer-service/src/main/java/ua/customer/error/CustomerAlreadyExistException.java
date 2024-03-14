package ua.customer.error;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerAlreadyExistException extends RuntimeException{
    public CustomerAlreadyExistException(String message) {
        super(message);
    }
}
