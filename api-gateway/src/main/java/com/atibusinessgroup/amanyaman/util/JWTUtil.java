package com.atibusinessgroup.amanyaman.util;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.atibusinessgroup.amanyaman.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JWTUtil {

    @Value("${springbootwebfluxjjwt.jjwt.secret}")
    private String secret;

    @Value("${springbootwebfluxjjwt.jjwt.expiration}")
    private String expirationTime;
    @Value("${springbootwebfluxjjwt.jjwt.expiration-rememberme}")
    private String expirationTimeRememberMe;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(User user, boolean rememberMe) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getAuthorities());
        claims.put(TokenKeyConstant.USER_LASTNAME_KEY, user.getLastName());
        claims.put(TokenKeyConstant.USER_FIRSTNAME_KEY, user.getFirstName());
        // if(user.getTravelAgent() != null){
        //     if(user.getTravelAgent().getId() != null){
        //         claims.put(USER_TRAVEL_AGENT, user.getTravelAgent().getId());
        //     }
        // }
        // claims.put(USER_TRAVEL_AGENT_STAFF, user.get().getId());
        return doGenerateToken(claims, user.getEmail(), rememberMe);
    }

    private String doGenerateToken(Map<String, Object> claims, String username, boolean rememberMe) {
        Long expirationTimeLong = 1000 * Long.parseLong(expirationTime); //in second
        Long expirationTimeLongRememberme = 1000 * Long.parseLong(expirationTimeRememberMe); //in second
        final Date createdDate = new Date();
        long now = createdDate.getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + expirationTimeLong);
        } else {
            validity = new Date(now + expirationTimeLongRememberme);
        }

        return Jwts.builder()
                .setSubject(username)
                .addClaims(claims)
                .setIssuedAt(createdDate)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

}