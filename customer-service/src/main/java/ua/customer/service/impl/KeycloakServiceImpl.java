package ua.customer.service.impl;


import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;


import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.AuthenticationManagementResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.admin.client.token.TokenManager;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.AuthDetailsRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import org.keycloak.representations.idm.authorization.AuthorizationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ua.customer.config.KeycloakConfig;
import ua.customer.dto.KeycloakUserRegisterRequest;
import ua.customer.error.CustomerAlreadyExistException;
import ua.customer.error.CustomerNotFoundException;
import ua.customer.service.KeycloakService;


import java.util.Collections;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {

    private final KeycloakConfig keycloakConfig;


    @Override
    public UserRepresentation findById(String id) {
        return getUserById(id);
    }

    @Override
    public UserRepresentation addUserToRealm(KeycloakUserRegisterRequest user) {
        UserRepresentation userRepresentation = new UserRepresentation();
        try (Response response =
                     getResource().create(createUserRepresentation(user))) {
            Optional.ofNullable(response)
                    .ifPresent(resp -> {
                        if (resp.getStatus() == HttpStatus.CREATED.value()) {
                            String userId = resp.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
                            userRepresentation.setId(userId);
                            sendVerifyEmail(userId);
                        } else if (resp.getStatus() == HttpStatus.CONFLICT.value()) {
                            throw new CustomerAlreadyExistException(resp.readEntity(String.class)
                                    .split("\"errorMessage\":\"")[1].split("\"")[0]);
                        }
                    });
        }
        return getUserById(userRepresentation.getId());
    }

    @Override
    public void updateRealmUser(String id, String firstName, String lastName) {
        UserRepresentation userRepresentation = getUserById(id);

        userRepresentation.setFirstName(firstName);
        userRepresentation.setLastName(lastName);

        getResource().get(id).update(userRepresentation);

    }


    private UserRepresentation createUserRepresentation(KeycloakUserRegisterRequest user) {
        UserRepresentation userRepresentation = new UserRepresentation();
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(user.getPassword());

        userRepresentation.setUsername(user.getUserName());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));
        userRepresentation.setEnabled(true);

        return userRepresentation;
    }


    private UserRepresentation getUserById(String id) {
        return Optional
                .ofNullable(getResource().get(id).toRepresentation())
                .orElseThrow(() -> new CustomerNotFoundException(" Customer with this id : " + id + " not found"));

    }

    @Override
    public void sendResetPasswordEmail(String id) {
        UserRepresentation userRepresentation = getUserById(id);
        getResource().get(userRepresentation.getId()).executeActionsEmail((List.of("UPDATE_PASSWORD")));
    }

    private void sendVerifyEmail(String id) {
        getResource().get(id).sendVerifyEmail();

    }

//    @Override
//    public String getToken(String userName, String password) {
//
//        UserRepresentation userRepresentation = getResource()
//                .searchByUsername(userName, false)
//                .getFirst();
//        boolean ignoreCase = userRepresentation
//                .getCredentials()
//                .getFirst()
//                .getValue()
//                .equalsIgnoreCase(password);
//        if (ignoreCase) {
//
//            AuthorizationResponse authorizationResponse = new AuthorizationResponse();
//            authorizationResponse.setToken(userRepresentation.getAttributes().get("grant_token").getFirst());
//
//
//        }
//
//
//    }

    private UsersResource getResource() {

        return keycloakConfig.getInstance().realm(keycloakConfig.getRealm()).users();
    }
}
