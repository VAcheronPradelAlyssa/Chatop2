package com.openclassrooms.chatop.service;

import com.openclassrooms.chatop.entity.User; // Assurez-vous que cette importation correspond à votre entité utilisateur
import com.openclassrooms.chatop.repository.UserRepository; // Assurez-vous que cette importation correspond à votre repository utilisateur
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service personnalisé pour la gestion des détails utilisateurs dans le cadre de l'authentification.
 * Implémente l'interface UserDetailsService de Spring Security.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository; // Injecte le repository utilisateur pour accéder aux données utilisateur

    /**
     * Charge un utilisateur en fonction de son email.
     * Cette méthode est utilisée par Spring Security pour effectuer l'authentification.
     *
     * @param email l'email de l'utilisateur
     * @return un objet UserDetails contenant les informations de l'utilisateur
     * @throws UsernameNotFoundException si aucun utilisateur n'est trouvé avec cet email
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Recherche de l'utilisateur dans la base de données par email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Retourne un objet UserDetails (utilisé par Spring Security pour l'authentification)
        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPassword()) // Mot de passe de l'utilisateur (déjà crypté dans la base de données)
                .authorities("USER") // Attribution d'un rôle ou d'une autorité (ici "USER", mais peut être modifié selon les rôles définis)
                .build();
    }
}
