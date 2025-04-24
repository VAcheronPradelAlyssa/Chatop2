package com.openclassrooms.chatop.service;

import com.openclassrooms.chatop.entity.User; // Assurez-vous que cette importation correspond à votre entité utilisateur
import com.openclassrooms.chatop.repository.UserRepository; // Assurez-vous que cette importation correspond à votre repository utilisateur
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository; // Injectez votre repository utilisateur

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Chargez l'utilisateur à partir de la base de données en utilisant l'email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Retournez un objet UserDetails
        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities("USER") // Ajoutez les rôles ou autorités appropriés
                .build();
    }
}
