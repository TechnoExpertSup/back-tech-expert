package ua.customer.dto.request;

import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@AllArgsConstructor
@Setter
@Getter
@ToString
public class CustomerRegisterRequest {

    @NotBlank(message = " User name can`t be empty ")
    @NotNull(message = " User name can`t be null ")
    @Size(min = 3, message = " User name must be at least 3 characters long ")
    private String userName;

    @NotBlank(message = " Email can`t be empty ")
    @NotNull(message = " Email can`t be null ")
    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,6})$",
            message = " Input correct email address ")
    private String email;

    private String firstName;

    private String lastName;

    @Size(min = 4, message = " Password must be at least 4 characters long ")
    @NotNull(message = "Password can`t be null")
    private String password;
}
