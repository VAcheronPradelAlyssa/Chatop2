package com.openclassrooms.chatop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Représente l’entité Messages dans la base de données, liant les messages à des locations et utilisateurs spécifiques.
 */
@Entity
@Table(name = "MESSAGES")
@EntityListeners(AuditingEntityListener.class) // Activer l’audit automatique (createdAt, updatedAt)
@Getter
@Setter
public class Messages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Clé primaire auto-incrémentée
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY) // Stratégie de récupération LAZYpour optimiser les performances
    @JoinColumn(name = "rental_id", nullable = false) // Clé étrangère à l’entité de location
    private Rental rental;

    @ManyToOne(fetch = FetchType.LAZY) // Stratégie de récupération LAZY pour optimiser les performances
    @JoinColumn(name = "user_id", nullable = false) // Clé étrangère à l’entité utilisateur
    private User user;

    @Column(length = 2000) // Spécifie la longueur maximale du contenu du message
    private String message;

    @CreatedDate // Capture automatiquement l’horodatage de la création
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate // Met automatiquement à jour l’horodatage lors de la modification
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}