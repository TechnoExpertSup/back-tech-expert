package ua.gatewayservice.config;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.minidev.json.JSONObject;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Configuration
public class FilterConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("modify_request_body", r -> r.path("/realms/technoExpert/protocol/openid-connect/token")
                        .filters(f -> f.modifyRequestBody(String.class, String.class, MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                                (exchange, s) -> {
                                    // Добавляем необходимые параметры к строке данных формы
                                    String modifiedFormData = s +
                                            "&grant_type=password" +
                                            "&client_id=gateway-service-client" +
                                            "&client_secret=ng8zu3qEVsJOFwIskclhHPlL31OKm94V";

                                    // Возвращаем модифицированную строку данных формы
                                    return Mono.just(modifiedFormData);
                                }))
                        .uri("http://localhost:8090/realms/technoExpert/protocol/openid-connect/token")) // изменяем URI
                .build();
    }
}