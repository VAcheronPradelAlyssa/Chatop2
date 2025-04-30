package com.openclassrooms.chatop.entity;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Identifiant unique de la location", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "Nom de la location", example = "Maison de rêve")
    private String name;

    @Schema(description = "Surface de la location en m²", example = "120.5")
    private Double surface;

    @Schema(description = "Prix de la location", example = "1500.0")
    private Double price;

    @Schema(description = "URL de l'image associée à la location", example = "http://example.com/image.jpg")
    private String picture;

    @Schema(description = "Description détaillée de la location", example = "Belle maison avec jardin")
    private String description;

   
    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
    private User owner;

    @Column(name = "created_at", updatable = false)
    @Schema(description = "Date et heure de création de la location", example = "2023-10-01T12:00:00")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Schema(description = "Date et heure de la dernière mise à jour de la location", example = "2023-10-01T12:00:00")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> message;
    
    /**
     * Méthode appelée automatiquement avant la création d’un enregistrement.
     */
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
