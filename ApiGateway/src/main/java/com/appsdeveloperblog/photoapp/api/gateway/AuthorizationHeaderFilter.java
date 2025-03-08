package com.appsdeveloperblog.photoapp.api.gateway;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    @Autowired
    Environment env;

    public AuthorizationHeaderFilter(){
        super(Config.class);
    }

    // business logic, accepts config
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();

            if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer", "").trim();

            /*List<String> authorites = getAuthorities(jwt);
            Boolean hasAuthority = authorites.stream()
                    .anyMatch(authority -> config.getAuthorities().contains(authority));
            if (!hasAuthority) {
                return onError(
                    exchange,
                    "Not authorized to perform operation.",
                    HttpStatus.FORBIDDEN);
            }*/

            /*String role = config.getRole();
            String authority = config.getAuthority();*/

            if(!isJwtValid(jwt)){
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {
        /*private List<String> authorities;

        public List<String> getAuthorities() {
            return authorities;
        }

        public void setAuthorities(String authorities) {
            this.authorities = Arrays.asList(authorities.split(" "));
        }

        private String role;
        private String authority;

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getAuthority() {
            return authority;
        }

        public void setAuthority(String authority) {
            this.authority = authority;
        }*/
    }

    // Gets role requirements for AuthorizationHeaderFilter from application.properties
    /*@Override
    public List<String> shortcutFieldOrder(){
        return Arrays.asList("authorities");
        return Arrays.asList("role", "authority");
    }*/

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus status){
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);

        /*DataBufferFactory dataBufferFactory = response.bufferFactory();
        DataBuffer dataBuffer = dataBufferFactory.wrap(err.getBytes());
        return response.writeWith(Mono.just(dataBuffer);*/

        return response.setComplete();
    }

    private boolean isJwtValid(String jwt) {
        boolean isValid = true;
        String subject = null;
        // Usually fetched from a Config server.
        String tokenSecret = env.getProperty("token.secret");
        byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
        //SecretKey signingKey = Keys.hmacShaKeyFor(secretKeyBytes);
        SecretKey signingKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build();

        try{
            subject = jwtParser.parseClaimsJws(jwt).getBody().getSubject();
        } catch (Exception e) {
            isValid = false;
        }

        if (subject == null || subject.isEmpty()){
            isValid = false;
        }

        return isValid;
    }

    /*private List<String> getAuthorities(String jwt) {
        List<String> returnValue = null;
        boolean isValid = true;
        String subject = null;
        // Usually fetched from a Config server.
        String tokenSecret = env.getProperty("token.secret");
        byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
        //SecretKey signingKey = Keys.hmacShaKeyFor(secretKeyBytes);
        SecretKey signingKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build();

        try{
            Jwt<Header, Claims> parsedToken = jwtParser.parse(jwt);
            List<Map<String, String>> scope = ((Claims) parsedToken.getBody()).get("scope", List.class);
            scope.stream()
                    .map(scopeMap -> returnValue.add(scopeMap.get("authority")))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return returnValue;
        }

        return returnValue;
    }*/
}
