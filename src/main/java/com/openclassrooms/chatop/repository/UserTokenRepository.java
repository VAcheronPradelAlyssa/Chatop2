package com.openclassrooms.chatop.repository;

import com.openclassrooms.chatop.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    Optional<UserToken> findByUserEmail(String email);  // Méthode pour trouver un token par email de l'utilisateur
}
