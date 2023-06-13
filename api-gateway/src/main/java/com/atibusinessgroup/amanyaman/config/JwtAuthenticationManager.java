package com.atibusinessgroup.amanyaman.config;

import java.util.List;
import java.util.stream.Collectors;

import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.atibusinessgroup.amanyaman.util.JWTUtil;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {
    
    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();

        try {
            String username = jwtUtil.getUsernameFromToken(authToken);
            jwtUtil.validateToken(authToken);
            Claims claims = jwtUtil.getAllClaimsFromToken(authToken);
            List<String> rolesMap = claims.get("role", List.class);

            return Mono.just(new UsernamePasswordAuthenticationToken(
                    username,
                    authToken,
                    rolesMap.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
            ));
        } catch (SignatureException e) {
            return Mono.error(new BadCredentialsException("Invalid token signature"));
        } catch (Exception e) {
            return Mono.error(new BadCredentialsException("Invalid token"));
        }
    }
}