package com.openclassrooms.chatop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO pour la réponse contenant les détails d'un message")
public class MessageResponse {

    @Schema(description = "Identifiant unique du message", example = "1")
    private Integer id;

    @Schema(description = "Identifiant unique de la location associée", example = "1")
    private Integer rentalId;

    @Schema(description = "Identifiant unique de l'utilisateur qui a créé le message", example = "1")
    private Integer userId;

    @Schema(description = "Contenu du message", example = "Ceci est un message")
    private String message;

    @Schema(description = "Date et heure de création du message", example = "2023-10-01T12:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Date et heure de la dernière mise à jour du message", example = "2023-10-01T12:00:00")
    private LocalDateTime updatedAt;
}
