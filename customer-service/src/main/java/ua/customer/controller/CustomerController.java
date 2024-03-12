package ua.customer.controller;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.customer.dto.CustomerRegisterRequest;
import ua.customer.repository.CustomerRepository;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerRepository customerRepository;

    @Value("${keycloak.auth-server-url}")
    private String keycloakBaseUrl;
    @Value("${keycloak.realm}")
    private  String realm;
    @Value("${keycloak.resource}")
    private  String clientId;
    @Value("${keycloak.credentials.secret}")
    private  String secret;

    @PostMapping("/save")
   public String save(@RequestBody CustomerRegisterRequest customer) throws Exception{
        System.out.println(customer.toString());
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakBaseUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(secret)
                .username("admin")
                .password("admin")
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();
   String accessTokenResponse  = keycloak.tokenManager().getAccessToken().getToken();
   Keycloak token = KeycloakBuilder.builder()
           .serverUrl(keycloakBaseUrl)
           .realm(realm)
           .clientId(clientId)
           .authorization("Bearer " + accessTokenResponse)
           .build();
        System.out.println(accessTokenResponse);
        UserRepresentation userRepresentation = new UserRepresentation();
        CredentialRepresentation credentialRepresentation =new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(customer.getPassword());

        userRepresentation.setUsername(customer.getUserName());
        userRepresentation.setEmail(customer.getEmail());
        userRepresentation.setFirstName(customer.getFirstName());
        userRepresentation.setLastName(customer.getLastName());
        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));
        userRepresentation.setEnabled(true);

        token.realm(realm).users().create(userRepresentation);

        keycloak.close();
        token.close();
        return "User created successfully";
    }
}
