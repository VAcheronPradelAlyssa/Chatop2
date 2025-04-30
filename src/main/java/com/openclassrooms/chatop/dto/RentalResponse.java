package com.openclassrooms.chatop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO pour renvoyer les informations d'une location.
 * Ce DTO contient les détails d'une location, y compris son nom, sa surface, son prix, etc.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO pour renvoyer les informations d'une location")
public class RentalResponse {

    @Schema(description = "Identifiant unique de la location", example = "1")
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

    @Schema(description = "Identifiant unique du propriétaire de la location", example = "1")
    private Integer owner_id;

    @Schema(description = "Date et heure de création de la location", example = "2023-10-01T12:00:00")
    private LocalDateTime created_at;

    @Schema(description = "Date et heure de la dernière mise à jour de la location", example = "2023-10-01T12:00:00")
    private LocalDateTime updated_at;
}
