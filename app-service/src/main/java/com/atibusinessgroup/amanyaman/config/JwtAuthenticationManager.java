package com.atibusinessgroup.amanyaman.config;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.atibusinessgroup.amanyaman.util.JWTUtil;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JwtAuthenticationManager implements AuthenticationManager {
    
	@Autowired
    private JWTUtil jwtUtil;

    @Override
    @SuppressWarnings("unchecked")
    public Authentication authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        String username = jwtUtil.getUsernameFromToken(authToken);
        boolean valid = jwtUtil.validateToken(authToken);
        if (!valid) {
            throw new IllegalArgumentException("Invalid authentication token");
        }

        Claims claims = jwtUtil.getAllClaimsFromToken(authToken);
        List<String> rolesMap = claims.get("role", List.class);

        return new UsernamePasswordAuthenticationToken(
            username,
            null,
            rolesMap.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
        );
    }
}
