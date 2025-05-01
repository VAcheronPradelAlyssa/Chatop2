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

/**
 * Classe de configuration de la sécurité Spring Security pour l'application.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    /**
     * Constructeur injectant le service de gestion des utilisateurs.
     * @param userDetailsService service permettant de récupérer les détails de l'utilisateur pour l'authentification
     */
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Configure le filtre de sécurité principal.
     *
     * - Active CORS
     * - Désactive CSRF
     * - Définit les routes publiques
     * - Configure JWT comme filtre d’authentification
     *
     * @param http configuration de la sécurité HTTP
     * @param jwtAuthenticationFilter filtre pour l’authentification JWT
     * @return la chaîne de filtres de sécurité
     * @throws Exception en cas d’erreur de configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
            .cors(withDefaults()) // Active la configuration CORS
            .csrf(csrf -> csrf.disable()) // Désactive la protection CSRF
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/register", "/api/auth/login").permitAll() // Routes publiques
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll() // Swagger UI autorisé
                .requestMatchers("/uploads/**").permitAll() // Autorise l'accès aux images
                .anyRequest().authenticated() // Toutes les autres routes nécessitent une authentification
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Pas de session HTTP, car on utilise JWT
            )
            .authenticationProvider(authenticationProvider()) // Fournisseur d'authentification personnalisé
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Ajoute le filtre JWT avant le filtre par défaut

        return http.build();
    }

    /**
     * Fournisseur d'authentification utilisant un DAO basé sur UserDetailsService.
     * @return le fournisseur d’authentification
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Charge les utilisateurs
        authProvider.setPasswordEncoder(passwordEncoder()); // Encodeur de mot de passe
        return authProvider;
    }

    /**
     * Configuration CORS permettant l'accès depuis le client Angular.
     * @return la source de configuration CORS
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200","http://localhost:3001")); // Origine autorisée
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // Méthodes autorisées
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type")); // Headers autorisés
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Applique à toutes les routes
        return source;
    }

    /**
     * Encodeur de mot de passe utilisant l'algorithme BCrypt.
     * @return l’encodeur de mot de passe
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
