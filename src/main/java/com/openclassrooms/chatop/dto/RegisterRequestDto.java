package com.openclassrooms.chatop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO pour la demande d'inscription d'un nouvel utilisateur.
 */
@Getter
@Setter
@Schema(description = "DTO pour la demande d'inscription d'un nouvel utilisateur")
public class RegisterRequestDto {

    /**
     * Le nom de l'utilisateur. Doit être fourni.
     */
    @NotBlank(message = "Name is required")
    @Schema(description = "Nom de l'utilisateur", required = true)
    private String name;

    /**
     * L'email de l'utilisateur. Doit être fourni et valide.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "Email de l'utilisateur", example = "user@example.com", required = true)
    private String email;

    /**
     * Le mot de passe de l'utilisateur. Doit être fourni.
     */
    @NotBlank(message = "Password is required")
    @Schema(description = "Mot de passe de l'utilisateur", required = true)
    private String password;
}
