package com.openclassrooms.chatop.service;

import com.openclassrooms.chatop.dto.UserDto;
import com.openclassrooms.chatop.dto.UserResponse;
import com.openclassrooms.chatop.entity.User;
import com.openclassrooms.chatop.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service pour la gestion des utilisateurs.
 * Fournit des méthodes pour enregistrer, récupérer et gérer les informations des utilisateurs.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Enregistre un nouvel utilisateur.
     *
     * @param userDto les données de l'utilisateur à enregistrer
     * @return l'utilisateur enregistré
     */
    public User registerUser(UserDto userDto) {
        // Encoder le mot de passe avant de l'enregistrer
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setPassword(encodedPassword);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    /**
     * Récupère un utilisateur par son identifiant.
     *
     * @param id l'identifiant de l'utilisateur
     * @return l'utilisateur trouvé ou null s'il n'existe pas
     */
    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Récupère un utilisateur par son email.
     *
     * @param email l'email de l'utilisateur
     * @return l'utilisateur trouvé ou Optional.empty() s'il n'existe pas
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Récupère l'utilisateur actuellement authentifié.
     *
     * @return l'utilisateur authentifié
     */
    public User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * Récupère le nom de l'utilisateur actuellement authentifié.
     *
     * @return le nom de l'utilisateur ou "Utilisateur non authentifié" si aucun utilisateur n'est authentifié
     */
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        } else {
            return "Utilisateur non authentifié";
        }
    }

    /**
     * Récupère une réponse utilisateur par son identifiant.
     *
     * @param id l'identifiant de l'utilisateur
     * @return la réponse utilisateur ou null si l'utilisateur n'existe pas
     */
    public UserResponse getUserResponseById(Integer id) {
        return userRepository.findById(id)
            .map(user -> new UserResponse(user.getId(), user.getName(), user.getEmail(), null, null))
            .orElse(null);
    }
}
