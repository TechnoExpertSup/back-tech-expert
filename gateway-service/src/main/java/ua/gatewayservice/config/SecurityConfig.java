package ua.gatewayservice.config;

import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import org.springframework.http.HttpMethod;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityGatewayFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange( auth -> auth
                        .pathMatchers(HttpMethod.POST, "/api/v1/customers/registration/**").permitAll()
                        .pathMatchers(HttpMethod.POST,"/realms/technoExpert/protocol/openid-connect/token").permitAll()
                        .anyExchange().authenticated()
                )
                .build();

    }


}
