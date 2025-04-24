package com.openclassrooms.chatop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.openclassrooms.chatop.security.JwtAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
            // Activation de CORS pour gérer les requêtes venant d'un autre domaine
            .cors(withDefaults())
            // Désactivation de CSRF, adapté pour une API REST sans état
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Permet l'accès public aux routes d'authentification (register, login, etc.)
                .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                // Permet l'accès public à Swagger UI et la documentation Swagger
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                // Permet l'accès public aux images téléchargées
                .requestMatchers("/uploads/**").permitAll()
                // Les autres routes nécessitent une authentification via JWT
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                // Session sans état, JWT gère l'authentification
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            // Authentification basée sur un fournisseur de token JWT
            .authenticationProvider(authenticationProvider())
            // Ajoute le filtre JWT avant le filtre d'authentification classique (UsernamePasswordAuthenticationFilter)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    
        return http.build();
    }
    

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));  // Permet l'origine de ton frontend
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
