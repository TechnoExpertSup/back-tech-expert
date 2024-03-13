package ua.customer.service.impl;


import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.customer.config.KeycloakConfig;
import ua.customer.dto.KeycloakUserRegisterRequest;
import ua.customer.error.CustomerAlreadyExistException;
import ua.customer.service.KeycloakService;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {

    private final KeycloakConfig keycloakConfig;
    @Value("${keycloak.realm}")
    private  String realm;

    @Override
    public void addUserToRealm(KeycloakUserRegisterRequest user) {

       try( Response response
                    = keycloakConfig.getInstance().realm(realm).users().create(createUserRepresentation(user))) {
           if (response != null && response.getStatus() == HttpStatus.SC_CREATED) {
               String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
               sendVerifyEmail(userId);
           }
       }catch (CustomerAlreadyExistException e){

           throw new CustomerAlreadyExistException(e.getMessage());

       }
    }

    @Override
    public void updateRealmUser(KeycloakUserRegisterRequest user) {

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


    private void sendVerifyEmail(String userId){

        keycloakConfig.getInstance().realm(realm).users().get(userId).sendVerifyEmail();
    }
}
