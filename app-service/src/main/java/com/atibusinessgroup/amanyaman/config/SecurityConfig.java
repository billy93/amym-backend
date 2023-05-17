package com.atibusinessgroup.amanyaman.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.atibusinessgroup.amanyaman.util.JWTUtil;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Configuration
@EnableMethodSecurity(
    prePostEnabled = true)
public class SecurityConfig  {
    private JwtAuthenticationManager authenticationManager;
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private AmanyamanSecurityContextRepository securityContextRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    try {
                        response.sendError(HttpStatus.FORBIDDEN.value());
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to send error response", e);
                    }
                })
            .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authenticationManager(authenticationManager)
                .securityContext().securityContextRepository(securityContextRepository)
            .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests()
                    .requestMatchers(HttpMethod.OPTIONS).permitAll()
                    .requestMatchers("/eureka/**",
                        // "/api/app/users/getByEmail/**",
                        // "/api/app/account/reset-password/init",
                        "/api/app/**").permitAll()
                    .anyRequest().authenticated();

        return http.build();
    }

    // @Override
    // protected void configure(HttpSecurity http) throws Exception {
    //     http
    //         .exceptionHandling()
    //             .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
    //             .accessDeniedHandler((request, response, accessDeniedException) -> response.sendError(HttpStatus.FORBIDDEN.value()))
    //         .and()
    //             .csrf().disable()
    //             .formLogin().disable()
    //             .httpBasic().disable()
    //             .authenticationManager(authenticationManager)
    //             .securityContext().securityContextRepository(securityContextRepository)
    //         .and()
    //             .addFilterBefore(new JwtAuthenticationFilter(authenticationManager, securityContextRepository), UsernamePasswordAuthenticationFilter.class)
    //             .authorizeRequests()
    //                 .requestMatchers(HttpMethod.OPTIONS).permitAll()
    //                 .requestMatchers("/eureka/**", 
    //                     // "/api/app/users/getByEmail/**",
    //                     // "/api/app/account/reset-password/init",
    //                     "/api/app/**").permitAll()
    //                 .anyRequest().authenticated();
    // }
    // @Bean
    // public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
    //     serverHttpSecurity
    //             .exceptionHandling()
    //             .authenticationEntryPoint((swe, e) -> 
    //                 Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED))
    //             )
    //             .accessDeniedHandler((swe, e) -> 
    //                 Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN))
    //             ).and()
    //             .csrf().disable()
    //             .formLogin().disable()
    //             .httpBasic().disable()
    //             .authenticationManager(authenticationManager)
    //             .securityContextRepository(securityContextRepository)
    //             .addFilterAt(new JwtAuthenticationFilter(authenticationManager, securityContextRepository), SecurityWebFiltersOrder.AUTHENTICATION)
    //             .authorizeExchange()
    //             .pathMatchers(HttpMethod.OPTIONS).permitAll()
    //             .pathMatchers("/eureka/**", 
    //             // "/api/app/users/getByEmail/**",
    //             // "/api/app/account/reset-password/init",
    //             "/api/app/**").permitAll()
    //             .anyExchange().authenticated();
    //     return serverHttpSecurity.build();
    // }
    
}
