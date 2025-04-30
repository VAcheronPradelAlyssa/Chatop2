package com.openclassrooms.chatop.repository;

import com.openclassrooms.chatop.entity.Messages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interface de dépôt pour l’entité Messages, fournissant des opérations CRUD
 * ainsi que des méthodes personnalisées pour interroger les messages selon la location ou l'utilisateur.
 */
public interface MessageRepository extends JpaRepository<Messages, Long> {

    /**
     * Récupère la liste des messages associés à une location spécifique.
     *
     * @param rentalId l’identifiant de la location concernée
     * @return une liste de messages liés à la location spécifiée
     */
    List<Messages> findByRentalId(Long rentalId);

    /**
     * Récupère la liste des messages envoyés par un utilisateur spécifique.
     *
     * @param userId l’identifiant de l’utilisateur
     * @return une liste de messages liés à l’utilisateur spécifié
     */
    List<Messages> findByUserId(Long userId);
}
