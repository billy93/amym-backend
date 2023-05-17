package com.atibusinessgroup.amanyaman.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.atibusinessgroup.amanyaman.util.JWTUtil;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final AmanyamanSecurityContextRepository securityContextRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractTokenFromRequest(request);
        if (token != null) {
            try {
                Claims claims = jwtUtil.getAllClaimsFromToken(token);
                String username = claims.getSubject();
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null);
                Authentication authenticated = authenticationManager.authenticate(authentication);
                SecurityContextHolder.getContext().setAuthentication(authenticated);
                securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);
            } catch (Exception e) {
                // Handle token validation failure
                // For example, you can log the error or return an unauthorized response
                // depending on your application's requirements.
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        // Extract the token from the request header or any other location based on your setup
        // and return it as a string
        // Example: Authorization header format: "Bearer <token>"
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Remove "Bearer " prefix
        }

        return null;
    }
}
