package com.atibusinessgroup.amanyaman.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {

    private JwtAuthenticationManager authenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange swe, SecurityContext sc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

//    @Override
//    public Mono<SecurityContext> load(ServerWebExchange swe) {
//        return Mono.justOrEmpty(swe.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
//            .filter(authHeader -> authHeader.startsWith("Bearer "))
//            .flatMap(authHeader -> {
//                String authToken = authHeader.substring(7);
//                Authentication auth = new UsernamePasswordAuthenticationToken(authToken, authToken);
//                return this.authenticationManager.authenticate(auth).map(SecurityContextImpl::new);
//            });
//    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange swe) {
        return Mono.justOrEmpty(swe.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(authHeader -> authHeader.startsWith("Bearer "))
                .flatMap(authHeader -> {
                    String authToken = authHeader.substring(7);
                    Authentication auth = new UsernamePasswordAuthenticationToken(authToken, authToken);
                    return this.authenticationManager.authenticate(auth)
                            .map(SecurityContextImpl::new)
                            .onErrorResume(AuthenticationException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials", e)))
                            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials")));
                });
    }

}