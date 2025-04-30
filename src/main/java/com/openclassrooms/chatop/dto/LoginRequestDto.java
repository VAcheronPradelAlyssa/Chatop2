package com.openclassrooms.chatop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO pour la demande de connexion de l'utilisateur.
 */
@Getter
@Setter
@Schema(description = "DTO pour la demande de connexion de l'utilisateur")
public class LoginRequestDto {

    /**
     * L'email de l'utilisateur pour la connexion.
     */
    @Schema(description = "Email de l'utilisateur", example = "user@example.com", required = true)
    private String email;

    /**
     * Le mot de passe de l'utilisateur pour la connexion.
     */
    @Schema(description = "Mot de passe de l'utilisateur", required = true)
    private String password;
}
