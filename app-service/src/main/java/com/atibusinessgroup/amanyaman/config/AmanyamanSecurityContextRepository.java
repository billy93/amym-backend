package com.atibusinessgroup.amanyaman.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.DeferredSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class AmanyamanSecurityContextRepository implements SecurityContextRepository {

    private final JwtAuthenticationManager authenticationManager;

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        HttpServletRequest request = requestResponseHolder.getRequest();
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String authToken = authHeader.substring(7);
            Authentication auth = new UsernamePasswordAuthenticationToken(authToken, authToken);
            Authentication authenticated = authenticationManager.authenticate(auth);
            return new SecurityContextImpl(authenticated);
        }
        return SecurityContextHolder.createEmptyContext();
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveContext'");
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        return true;
    }

    // @Override
    // public void save(ServerWebExchange exchange, SecurityContext context) {
    //     throw new UnsupportedOperationException("Not supported yet.");
    // }

    // @Override
    // public Authentication load(ServerWebExchange exchange) {
    //     String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    //     if (authHeader != null && authHeader.startsWith("Bearer ")) {
    //         String authToken = authHeader.substring(7);
    //         Authentication auth = new UsernamePasswordAuthenticationToken(authToken, authToken);
    //         return authenticationManager.authenticate(auth);
    //     }
    //     return null;
    // }
}