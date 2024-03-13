package ua.customer.dto;

import lombok.Data;

@Data
public class KeycloakUserRegisterRequest {
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
}
