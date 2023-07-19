package net.franzka.kams.gateway.filter;

import lombok.extern.log4j.Log4j2;
import net.franzka.kams.gateway.service.JwtService;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final JwtService jwtService;

    public AuthenticationFilter(JwtService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            //header contains token or not
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new RuntimeException("Missing authorization header !");
            }

            String authHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String token = "";
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }

            try {
                jwtService.validateToken(token);
                request = exchange.getRequest()
                        .mutate()
                        .header("loggedInUserEmail", jwtService.getLoggedUserEmail(token))
                        .build();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }

            return chain.filter(exchange.mutate().request(request).build());
        });
    }

    public static class Config {

    }
}
