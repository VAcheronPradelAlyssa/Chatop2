package com.openclassrooms.chatop.security;

import com.openclassrooms.chatop.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtre d'authentification JWT qui s'assure que l'utilisateur est authentifié à chaque requête HTTP
 * en vérifiant la présence d'un token JWT valide dans l'en-tête "Authorization".
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Constructeur du filtre d'authentification JWT.
     * @param jwtService le service de gestion des tokens JWT
     * @param userDetailsService le service de gestion des détails utilisateur
     */
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Méthode principale du filtre, exécutée à chaque requête HTTP.
     * Elle extrait le token JWT de l'en-tête de la requête, vérifie sa validité et
     * l'associe à un utilisateur authentifié dans le contexte de sécurité de Spring.
     *
     * @param request la requête HTTP
     * @param response la réponse HTTP
     * @param filterChain chaîne de filtres de requêtes HTTP
     * @throws ServletException en cas d'erreur lors de la gestion de la requête
     * @throws IOException en cas d'erreur d'entrée/sortie
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String jwt = null;
        String email = null;

        // Vérifie si l'en-tête Authorization est présent et commence par "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);  // Extrait le token JWT
            email = jwtService.extractEmailFromToken(jwt);  // Extrait l'email du token
        }

        // Si un email est extrait et qu'aucune authentification n'est encore en cours
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Charge les détails de l'utilisateur basé sur l'email
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // Si le token est valide, on crée un objet d'authentification
            if (jwtService.isTokenValid(jwt)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // On assigne l'authentification au contexte de sécurité
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // Poursuit le processus de filtre de la requête
        filterChain.doFilter(request, response);
    }
}
