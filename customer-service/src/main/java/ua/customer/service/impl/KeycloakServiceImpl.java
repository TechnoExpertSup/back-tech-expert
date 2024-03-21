package ua.customer.service.impl;


import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;


import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ua.customer.config.KeycloakConfig;
import ua.customer.error.CustomerAlreadyExistException;
import ua.customer.error.CustomerNotFoundException;
import ua.customer.service.KeycloakService;


import java.util.List;
import java.util.NoSuchElementException;
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
    public UserRepresentation addUserToRealm(UserRepresentation user) {
        try (Response response =
                     getResource().create(user)) {
            Optional.ofNullable(response)
                    .ifPresent(resp -> {
                        if (resp.getStatus() == HttpStatus.CREATED.value()) {
                            String userId = resp.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
                            user.setId(userId);
                            sendVerifyEmail(user.getId());
                        } else if (resp.getStatus() == HttpStatus.CONFLICT.value()) {
                            throw new CustomerAlreadyExistException(resp.readEntity(String.class)
                                    .split("\"errorMessage\":\"")[1].split("\"")[0]);
                        }
                    });
        }
        return user;
    }

    @Override
    public void updateRealmUser(String id, String firstName, String lastName) {
        UserRepresentation userRepresentation = getUserById(id);

        userRepresentation.setFirstName(firstName);
        userRepresentation.setLastName(lastName);

        getResource().get(id).update(userRepresentation);

    }


    private UserRepresentation getUserById(String id) {
      try {
          return getResource().get(id).toRepresentation();
      }catch (NotFoundException exception){
          throw new CustomerNotFoundException(" Customer with this id : " + id + " not found");
      }
    }

    @Override
    public void sendResetPasswordEmail(String email) {
       try {
           Optional.ofNullable(getResource().searchByEmail(email, false).stream().findFirst().get())
                   .ifPresent(userRepresentation -> getResource().get(userRepresentation.getId())
                           .executeActionsEmail((List.of("UPDATE_PASSWORD"))));
       }catch (NoSuchElementException exception){
           throw new CustomerNotFoundException(" Customer with this email : " + email + " not found");
       }

    }
    @Async
    protected void sendVerifyEmail(String id) {
        getResource().get(id).sendVerifyEmail();

    }

    private UsersResource getResource() {

        return keycloakConfig.getInstance().realm(keycloakConfig.getRealm()).users();
    }
}
