package com.openclassrooms.chatop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * DTO représentant les données de réponse de l'utilisateur.
 * Cette classe est utilisée pour envoyer les détails de l'utilisateur dans le corps de la réponse après une requête réussie.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO pour la réponse contenant les détails de l'utilisateur")
public class UserResponse {

    @Schema(description = "Identifiant unique de l'utilisateur", example = "123", required = true)
    private Integer id;

    @Schema(description = "Nom de l'utilisateur", example = "John Doe", required = true)
    private String name;

    @Schema(description = "Email de l'utilisateur", example = "user@example.com", required = true)
    private String email;

    @Schema(description = "Date et heure de création de l'utilisateur", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime created_at;

    @Schema(description = "Date et heure de la dernière mise à jour de l'utilisateur", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime updated_at;
}
