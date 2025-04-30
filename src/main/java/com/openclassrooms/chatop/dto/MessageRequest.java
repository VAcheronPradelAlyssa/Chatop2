package com.openclassrooms.chatop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO représentant la demande de création d'un message.
 * Contient les informations nécessaires à la création d'un message dans une location donnée.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO pour la demande de création d'un message")
public class MessageRequest {


    @NotNull(message = "Rental ID must be provided.")
    @Schema(description = "ID de la location associée au message", required = true)
    private Integer rental_id;


    @NotNull(message = "User ID must be provided.")
    @Schema(description = "ID de l'utilisateur qui crée le message", required = true)
    private Integer user_id;


    @Size(max = 2000, message = "Message content must not exceed 2000 characters.")
    @Schema(description = "Contenu du message", maxLength = 2000, required = true)
    private String message;
}
