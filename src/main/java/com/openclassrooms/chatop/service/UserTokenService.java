package com.openclassrooms.chatop.service;

import com.openclassrooms.chatop.entity.User;
import com.openclassrooms.chatop.entity.UserToken;
import com.openclassrooms.chatop.repository.UserTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserTokenService {

    private final UserTokenRepository userTokenRepository;

    @Autowired
    public UserTokenService(UserTokenRepository userTokenRepository) {
        this.userTokenRepository = userTokenRepository;
    }

    /**
     * Enregistre un token pour un utilisateur donné.
     * @param user L'utilisateur auquel le token appartient
     * @param token Le token à enregistrer
     */
    public void saveUserToken(User user, String token) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = now.plusHours(10); // Exemple de durée de validité

        UserToken userToken = UserToken.builder()
                .user(user)  // L'utilisateur associé
                .token(token)  // Le token à enregistrer
                .createdAt(now)  // Heure de création
                .expiresAt(expirationTime)  // Heure d'expiration
                .build();

        userTokenRepository.save(userToken);  // Sauvegarde du token dans la base de données
    }
}
