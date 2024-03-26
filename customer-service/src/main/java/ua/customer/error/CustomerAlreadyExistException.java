package ua.customer.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomerAlreadyExistException extends RuntimeException{
    private String message;

}
