package ua.customer.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.customer.dto.response.CustomerResponse;
import ua.customer.dto.request.CustomerRegisterRequest;
import ua.customer.dto.request.CustomerUpdateRequest;
import ua.customer.mapper.CustomerMapper;
import ua.customer.service.KeycloakService;

import java.net.URI;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private final CustomerMapper customerMapper = CustomerMapper.INSTANCE;

    private final KeycloakService keycloakService;


    @GetMapping("/{id}")
    public ResponseEntity<UserRepresentation> customerById(@PathVariable String id) {

        return ResponseEntity.ok(keycloakService.findById(id));
    }


    @PostMapping("/registration")
    public ResponseEntity<CustomerResponse<UserRepresentation>> registration(@Valid @RequestBody CustomerRegisterRequest user,
                                                                             HttpServletRequest request) {

        UserRepresentation savedUser = keycloakService.addUserToRealm(customerMapper.toUserRepresentation(user));

        return ResponseEntity
                .created(URI.create(request.getRequestURL().toString() + "/" + savedUser.getId())).
                body(new CustomerResponse<>(savedUser
                        , "A confirmation email has been sent to your email address. Please verify your account"));

    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCustomerFirstLastNames(@PathVariable String id, CustomerUpdateRequest user) {
        keycloakService.updateRealmUser(id, user.getFirstName(), user.getLastName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reset-password/{email}")
    public ResponseEntity<String> resetCustomerPassword(@PathVariable String email) {
        keycloakService.sendResetPasswordEmail(email);
        return ResponseEntity.ok("An email with instructions on how to change your password has been sent to your email address.");
    }


}
