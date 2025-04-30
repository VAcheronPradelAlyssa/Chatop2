package com.openclassrooms.chatop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Représente l’entité "Message" dans la base de données.
 * Un message est associé à une location spécifique et un utilisateur.
 * L'audit automatique est activé pour les dates de création et de mise à jour.
 */
@Entity
@Table(name = "MESSAGES")
@EntityListeners(AuditingEntityListener.class) // Active la gestion automatique des dates (création et modification)
@Getter
@Setter
public class Messages {

    /**
     * Identifiant unique du message, généré automatiquement.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Clé primaire auto-incrémentée
    private Integer id;

    /**
     * Location associée au message.
     * Chaque message est lié à une location précise via la clé étrangère "rental_id".
     */
    @ManyToOne(fetch = FetchType.LAZY) // LAZY loading pour optimiser les performances lors de la récupération
    @JoinColumn(name = "rental_id", nullable = false) // La clé étrangère à l’entité Rental
    private Rental rental;

    /**
     * Utilisateur qui a écrit le message.
     * Chaque message est lié à un utilisateur via la clé étrangère "user_id".
     */
    @ManyToOne(fetch = FetchType.LAZY) // LAZY loading pour optimiser les performances
    @JoinColumn(name = "user_id", nullable = false) // La clé étrangère à l’entité User
    private User user;

    /**
     * Contenu du message.
     * La longueur maximale du message est limitée à 2000 caractères pour éviter des messages trop longs.
     */
    @Column(length = 2000) // Limite de longueur pour éviter des messages trop longs
    private String message;

    /**
     * Date et heure de création du message, automatiquement définie lors de la création.
     */
    @CreatedDate // L'annotation permet de capturer la date de création automatiquement
    @Column(name = "created_at", updatable = false) // Cette colonne ne doit pas être mise à jour après la création
    private LocalDateTime createdAt;

    /**
     * Date et heure de la dernière mise à jour du message.
     * Elle est automatiquement mise à jour lors de toute modification du message.
     */
    @LastModifiedDate // L'annotation permet de capturer la date de la dernière modification automatiquement
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
