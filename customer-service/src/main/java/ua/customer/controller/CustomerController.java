package ua.customer.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.customer.dto.response.CustomerResponse;
import ua.customer.dto.request.CustomerRegisterRequest;
import ua.customer.dto.request.CustomerUpdateRequest;
import ua.customer.mapper.CustomerMapper;
import ua.customer.service.KeycloakService;

import java.net.URI;
import java.security.Principal;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
@Slf4j
@Validated
public class CustomerController {
    private final CustomerMapper customerMapper = CustomerMapper.INSTANCE;

    private final KeycloakService keycloakService;

    @GetMapping("/{id}")
    public ResponseEntity<UserRepresentation> customerById(@Size(min = 36) @PathVariable String id) {
        log.info("Received request to get customer by ID: {}", id);
        return ResponseEntity
                .ok(keycloakService.findById(id));
    }


    @PostMapping("/registration")
    public ResponseEntity<CustomerResponse<UserRepresentation>> registration(@Valid @RequestBody CustomerRegisterRequest customer,
                                                                             HttpServletRequest request) {
        log.info("A registration request has been received. Customer : {}", customer);
        UserRepresentation savedUser = keycloakService.addCustomerToRealm(customerMapper.toUserRepresentation(customer));
        log.info("Registration has been successful. New customer id : {}", savedUser.getId());
        return ResponseEntity
                .created(URI
                        .create(request.getRequestURL().toString() + "/" + savedUser.getId()))
                .body(new CustomerResponse<>(savedUser
                        , "A confirmation email has been sent to your email address. Please verify your account"));

    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCustomerFirstLastNames(@Size(min = 36) @PathVariable String id
            , @RequestBody CustomerUpdateRequest customer) {
        log.info("Received request to update customer with id : {} , first name : {} , and last name : {}"
                , customer.getFirstName(), customer.getLastName(), id);
        keycloakService.updateCustomer(id, customer.getFirstName(), customer.getLastName());
        log.info(" Update has been successful ");
        return ResponseEntity
                .noContent()
                .build();
    }


    @PostMapping("/reset-password")
    public ResponseEntity<String> resetCustomerPassword(@Email @RequestBody String email) {
        log.info("Received request to reset customer password with email : {}", email);
        keycloakService.sendResetPasswordEmail(email);
        log.info(" The password reset email was sent successfully.");
        return ResponseEntity.
                ok("An email with instructions on how to change your password has been sent to your email address.");
    }


}
