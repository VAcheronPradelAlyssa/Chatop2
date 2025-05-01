package com.openclassrooms.chatop.service;

import com.openclassrooms.chatop.dto.RegisterRequestDto;
import com.openclassrooms.chatop.dto.RegisterResponseDto;
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
    @Autowired
    private JwtService jwtService;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

      /**
     * Enregistre un nouvel utilisateur.
     *
     * @param request les données de l'utilisateur à enregistrer
     * @return une réponse contenant le token JWT et l'ID de l'utilisateur
     */
    public RegisterResponseDto registerUser(RegisterRequestDto request) {
        // Vérifier si l'email existe déjà
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà.");
        }

        // Créer un nouvel utilisateur
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // Sauvegarder l'utilisateur dans la base de données
        user = userRepository.save(user);

        // Générer un token JWT pour l'utilisateur
        String token = jwtService.generateToken(user.getEmail());

        // Retourner une réponse avec le token et l'ID de l'utilisateur
        return new RegisterResponseDto(token, user.getId());
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
