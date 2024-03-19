package ua.customer.service;

import org.keycloak.representations.idm.UserRepresentation;

public interface KeycloakService {

    UserRepresentation findById(String id);
    UserRepresentation addUserToRealm(UserRepresentation user);
    void updateRealmUser(String id,String firstName, String lastName);

    public void sendResetPasswordEmail(String email);



}
