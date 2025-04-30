package com.openclassrooms.chatop.repository;

import com.openclassrooms.chatop.entity.Messages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interface de dépôt pour l’entité Messages, fournissant des opérations CRUD et des requêtes personnalisées.
 */
public interface MessageRepository extends JpaRepository<Messages, Long> {

    /**
     * Trouve une liste de messages associés à une location spécifique par ID de location.
     * @param rentalId l’ID de la location
     * @return une liste de messages liés à la location spécifiée
     */
    List<Messages> findByRentalId(Long rentalId);

    /**
     * Trouve une liste de messages associés à un utilisateur spécifique par ID utilisateur.
     * @param userId l’ID de l’utilisateur
     * @return une liste de messages liés à l’utilisateur spécifié
     */
    List<Messages> findByUserId(Long userId);
}