package com.openclassrooms.chatop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO contenant les détails d'une location.
 * Inclut les informations de base ainsi que les détails du propriétaire.
 */
@Data
@Schema(description = "DTO contenant les détails d'une location")
public class RentalDto {

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

    @Schema(description = "Identifiant unique du propriétaire de la location", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer owner_id;

    @Schema(description = "Nom du propriétaire de la location", example = "Jean Dupont")
    private String owner_name;

    @Schema(description = "Email du propriétaire de la location", example = "owner@example.com")
    private String ownerEmail;

    @Schema(description = "Date et heure de la dernière mise à jour de la location", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updated_at;

    @Schema(description = "Date et heure de création de la location", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime created_at;


}
