package ua.gatewayservice.config;


import org.springframework.cloud.gateway.filter.GatewayFilter;
;


import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


//@Component
//public class TokenAuthenticationFilter extends AbstractGatewayFilterFactory {
//
//
//    @Override
//    public GatewayFilter apply(Object config) {
//        return (exchange, chain) -> exchange.getFormData()
//                .flatMap(data -> {
//                    try {
//                        data.add("additional_key", "additional_value");
//                        // Adjust grant type based on your authentication flow (e.g., "client_credentials")
//                        data.add("grant_type", "password"); // May not be suitable for all flows
//                        data.add("client_id", "gateway-service-client");
//                        data.add("client_secret", "ng8zu3qEVsJOFwIskclhHPlL31OKm94V");
//                        // Omit clientSecret from code (use secure storage)
//                        return chain.filter(exchange);
//                    } catch (Exception e) {
//                        // Handle exception
//                        return Mono.error(e);
//                    }
//                });
//    }
//
//
//    @Override
//    public String name() {
//        return "tokenAuthenticationFilter";
//    }
//}

