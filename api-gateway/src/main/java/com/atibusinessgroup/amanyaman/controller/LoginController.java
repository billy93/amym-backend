package com.atibusinessgroup.amanyaman.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.atibusinessgroup.amanyaman.config.JwtTokenProvider;
import com.atibusinessgroup.amanyaman.dto.AuthResponse;
import com.atibusinessgroup.amanyaman.util.JWTUtil;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
public class LoginController {

    @Autowired
    private JWTUtil jwtUtil;

    public LoginController() {
    }

    @PostMapping("/login")
    public Mono<AuthResponse> login(@RequestBody UserCredentials credentials) {
        if ("user".equals(credentials.getUsername()) && "password".equals(credentials.getPassword())) {
            // List<String> authorities = Collections.singletonList("ROLE_USER");
            // String token = jwtTokenProvider.createToken(credentials.getUsername(), authorities);
            // return Mono.just(token);

            return Mono.just(new AuthResponse(jwtUtil.generateToken()));
        } else {
            return Mono.empty();
        }
    }

    private static class UserCredentials {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
