package com.openclassrooms.chatop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO représentant la réponse après une connexion réussie.
 * Contient le token JWT de l'utilisateur et son identifiant.
 */
@Getter
@AllArgsConstructor
@Schema(description = "DTO pour la réponse après une connexion réussie")
public class LoginResponseDto {


    @Schema(description = "Token JWT pour l'utilisateur", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", required = true)
    private String token;

    @Schema(description = "Identifiant de l'utilisateur", example = "123", required = true)
    private Integer userId;
}
