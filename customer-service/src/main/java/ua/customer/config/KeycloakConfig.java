package ua.customer.config;

import lombok.Getter;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class KeycloakConfig {

    @Value("${keycloak.auth-server-url}")
    private String keycloakBaseUrl;
    @Getter
    @Value("${keycloak.realm}")
    private  String realm;
    @Value("${keycloak.resource}")
    private  String clientId;
    @Value("${keycloak.credentials.secret}")
    private  String secret;
    @Value("${keycloak.username}")
    private String username;
    @Value("${keycloak.password}")
    private String password;

    @Bean
    public Keycloak getInstance(){

     return KeycloakBuilder.builder()
               .serverUrl(keycloakBaseUrl)
               .realm(realm)
               .clientId(clientId)
               .clientSecret(secret)
               .username(username)
               .password(password)
               .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
               .build();
    }

}
