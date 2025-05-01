package com.openclassrooms.chatop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO pour représenter un utilisateur.
 * Ce DTO contient les informations essentielles nécessaires à la création ou la mise à jour d'un utilisateur.
 */
@Getter
@Setter
@Schema(description = "DTO pour représenter un utilisateur")
public class UserDto {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "Email de l'utilisateur", example = "user@example.com", required = true)
    private String email;

   
    @NotBlank(message = "Name is required")
    @Schema(description = "Nom de l'utilisateur", required = true)
    private String name;

    @NotBlank(message = "Password is required")
    @Schema(description = "Mot de passe de l'utilisateur", required = true)
    private String password;
}
