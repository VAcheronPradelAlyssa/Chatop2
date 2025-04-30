package com.openclassrooms.chatop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la réponse à la création d'un utilisateur.
 * Contient le jeton JWT généré après l'enregistrement et l'ID de l'utilisateur créé.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO pour la réponse à la création d'un utilisateur")
public class RegisterResponseDto {

    /**
     * Le jeton JWT qui permet à l'utilisateur de s'authentifier dans les futures requêtes.
     */
    @Schema(description = "Token JWT pour l'utilisateur", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", required = true)
    private String token;

    /**
     * L'identifiant unique de l'utilisateur nouvellement créé.
     */
    @Schema(description = "Identifiant de l'utilisateur", example = "123", required = true)
    private Integer userId;
}
