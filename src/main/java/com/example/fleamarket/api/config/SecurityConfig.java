package com.example.fleamarket.api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@ConditionalOnWebApplication
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Import(SecurityDependenciesConfig.class)
public class SecurityConfig {
    private final AuthenticatedUserAuthenticationConverter authenticatedUserAuthenticationConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtDecoder jwtDecoder) throws Exception {

        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/sell/**").authenticated()
                .requestMatchers("/catalog/**").permitAll()
                .requestMatchers("/buy/**").authenticated()
                .requestMatchers("/shipping/**").authenticated()
                .requestMatchers("/review/rated-users/*").permitAll()
                .requestMatchers("/review/**").authenticated()
                .requestMatchers("/user/users/**").authenticated()
                // Spring BootのBasicErrorControllerを呼びされるようにする
                .requestMatchers("/error").permitAll()
                .anyRequest().denyAll()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder)
                .jwtAuthenticationConverter(this.authenticatedUserAuthenticationConverter))

            );
        http.csrf(csrf -> csrf.disable());

        return http.build();
    }

}
