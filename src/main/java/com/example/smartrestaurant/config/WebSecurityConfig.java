package com.example.smartrestaurant.config;

import com.example.smartrestaurant.security.jwt.AuthEntryPointJwt;
import com.example.smartrestaurant.security.jwt.AuthTokenFilter;
import com.example.smartrestaurant.security.jwt.JwtUtils;
import com.example.smartrestaurant.security.UserDetailsServiceImpl;
import com.example.smartrestaurant.security.services.TokenRevocationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, AuthEntryPointJwt entryPoint,
                                    JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService,
                                    TokenRevocationService tokenRevocationService) throws Exception {
        AuthTokenFilter authTokenFilter = new AuthTokenFilter(
                jwtUtils, userDetailsService, tokenRevocationService);
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e -> e.authenticationEntryPoint(entryPoint))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login", "/error", "/img/**").permitAll()
                        .requestMatchers("/api/auth/register").hasAnyRole("ADMIN", "OWNER")
                        .anyRequest().authenticated())
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
