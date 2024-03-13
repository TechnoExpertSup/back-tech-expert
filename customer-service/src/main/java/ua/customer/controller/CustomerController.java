package ua.customer.controller;


import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.customer.dto.KeycloakUserRegisterRequest;
import ua.customer.repository.CustomerRepository;
import ua.customer.service.KeycloakService;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final KeycloakService keycloakService;



    @PostMapping("/save")
   public String save(@RequestBody KeycloakUserRegisterRequest user) {

        keycloakService.addUserToRealm(user);

        return "User created successfully";
    }
}
