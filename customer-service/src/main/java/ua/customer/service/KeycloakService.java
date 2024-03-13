package ua.customer.service;

import ua.customer.dto.KeycloakUserRegisterRequest;

public interface KeycloakService {

    void addUserToRealm(KeycloakUserRegisterRequest user);
    void updateRealmUser(KeycloakUserRegisterRequest user);


}
