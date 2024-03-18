package ua.gatewayservice.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import reactor.core.publisher.Mono;

@Configuration
public class FilterConfig {

    @Value("${spring.security.oauth2.auth.uriToken}")

    private  String pathRedirect;
    @Value("${spring.security.oauth2.auth.grant_type}")
    private String grantType;
    @Value("${spring.security.oauth2.client.registration.gateway-service.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.gateway-service.client-secret}")

    private String clientSecret;
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("modify_request_body", r -> r.path("/realms/technoExpert/protocol/openid-connect/token")
                        .filters(f -> f.modifyRequestBody(String.class, String.class,
                                MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                                (exchange, s) -> {
                                    String modifiedFormData = forModifyRequestBody(s);
                                    return Mono.just(modifiedFormData);
                                }))
                        .uri(pathRedirect))
                .build();
    }

    private String forModifyRequestBody(String userNameAndPassword){
        StringBuilder modifyString = new StringBuilder();
        String password = userNameAndPassword.substring(userNameAndPassword.indexOf('=') + 1);
        String userName = userNameAndPassword.substring(userNameAndPassword.indexOf('='));
        modifyString.append("username=").append(userName);
        modifyString.append("&password").append(new BCryptPasswordEncoder().encode(password));
        modifyString.append("&grant_type=").append(grantType);
        modifyString.append("&client_id=").append(clientId);
        modifyString.append("&client_secret=").append(clientSecret);

        return modifyString.toString();
    }


}