package com.atibusinessgroup.amanyaman.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.util.pattern.PathPatternParser;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowCredentials(true);
        corsConfig.addAllowedOriginPattern("*"); // Use allowedOriginPatterns instead
        corsConfig.addAllowedHeader("*");
        corsConfig.addAllowedMethod("*");

        CorsConfigurationSource source = request -> corsConfig;

        return new CorsWebFilter(source);
    }
}
