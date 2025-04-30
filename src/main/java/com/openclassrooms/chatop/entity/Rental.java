package com.openclassrooms.chatop.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entité représentant une location (rental) dans l'application.
 * Elle contient des informations sur la propriété ainsi que les relations avec l'utilisateur propriétaire et les messages associés.
 */
@Data
@Entity
@Table(name = "RENTALS")
public class Rental {

    /**
     * Identifiant unique de la location (clé primaire).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Integer id;
    private String name;
    private Double surface;
    private Double price;
    private String picture;
    private String description;

    /**
     * Liste des messages liés à cette location.
     * Relation un-à-plusieurs avec cascade de toutes les opérations et suppression des orphelins.
     */
    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Messages> messages;

    /**
     * Propriétaire de la location (utilisateur).
     * Relation plusieurs-à-un avec l'entité User.
     */
    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
    private User owner;
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Méthode appelée automatiquement avant la mise à jour d’un enregistrement.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
