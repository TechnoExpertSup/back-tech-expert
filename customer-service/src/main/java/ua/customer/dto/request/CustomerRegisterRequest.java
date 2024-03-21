package ua.customer.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;



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

    public CustomerRegisterRequest(String userName, String email, String firstName, String lastName, String password) {
        this.userName = userName;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CustomerRegisterRequest() {
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
