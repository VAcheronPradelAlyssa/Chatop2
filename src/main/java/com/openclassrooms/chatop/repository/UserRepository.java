package com.openclassrooms.chatop.repository;

import com.openclassrooms.chatop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Interface de dépôt pour l’entité User.
 * Fournit les opérations CRUD de base via JpaRepository, ainsi que des méthodes personnalisées pour la gestion des utilisateurs.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Recherche un utilisateur par son adresse email.
     * @param email l'email de l'utilisateur à rechercher
     * @return un Optional contenant l'utilisateur si trouvé, sinon vide
     */
    Optional<User> findByEmail(String email);

    /**
     * Vérifie si un utilisateur existe déjà avec l'adresse email spécifiée.
     * @param email l'email à vérifier
     * @return true si un utilisateur avec cet email existe, false sinon
     */
    boolean existsByEmail(String email);

}
