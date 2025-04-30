package com.openclassrooms.chatop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO pour renvoyer les détails du message en réponse.
 * Contient des informations sur le message, y compris la location et l’utilisateur associés.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO pour la réponse contenant les détails du message")
public class MessageResponse {

    @Schema(description = "Identifiant unique du message", example = "1")
    private Integer id;

    @Schema(description = "ID de la location associée", example = "1")
    private Integer rental;

    @Schema(description = "ID de l’utilisateur qui a créé le message", example = "1")
    private Integer user;

    @Schema(description = "Contenu du message", example = "Ceci est un message")
    private String message;

    @Schema(description = "Date et heure de création du message", example = "2023-10-01T12:00:00")
    private LocalDateTime created_at;

    @Schema(description = "Date de la dernière mise à jour du message", example = "2023-10-01T12:00:00")
    private LocalDateTime updated_at;
}
