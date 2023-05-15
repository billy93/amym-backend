package com.atibusinessgroup.amanyaman.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;

import com.atibusinessgroup.amanyaman.util.JWTUtil;

import reactor.core.publisher.Mono;

public class JwtAuthenticationFilter extends AuthenticationWebFilter {
    @Autowired
    private JWTUtil jwtUtil;

    
    public JwtAuthenticationFilter(ReactiveAuthenticationManager authenticationManager, ServerSecurityContextRepository securityContextRepository) {
        super(authenticationManager);
        setServerAuthenticationConverter(this::convert);
    }

    private Mono<Authentication> convert(ServerWebExchange exchange) {
        String token = extractTokenFromRequest(exchange.getRequest());        
        if (token != null) {
            try {
                Claims claims = jwtUtil.getAllClaimsFromToken(token);
                String username = claims.getSubject();
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null);
                return Mono.just(authentication);
            } catch (Exception e) {
                // Handle token validation failure
                // For example, you can log the error or return an unauthorized response
                // depending on your application's requirements.
            }
        }

        return Mono.empty();
    }

    private String extractTokenFromRequest(ServerHttpRequest request) {
        // Extract the token from the request header or any other location based on your setup
        // and return it as a string
        // Example: Authorization header format: "Bearer <token>"
        String authorizationHeader = request.getHeaders().getFirst("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Remove "Bearer " prefix
        }

        return null;
    }
}
