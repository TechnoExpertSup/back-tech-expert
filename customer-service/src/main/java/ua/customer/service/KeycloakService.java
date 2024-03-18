package ua.customer.service;

import org.keycloak.representations.idm.UserRepresentation;
import ua.customer.dto.KeycloakUserRegisterRequest;

public interface KeycloakService {

    UserRepresentation findById(String id);
    UserRepresentation addUserToRealm(KeycloakUserRegisterRequest user);
    void updateRealmUser(String id,String firstName, String lastName);

    public void sendResetPasswordEmail(String id);
   // String getToken(String userName,String password);

}
