package com.openclassrooms.chatop.dto;

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
public class MessageResponse {

    private Integer id; // Unique identifier of the message
    private Integer rental; // ID de la location associée
    private Integer user; // ID de l’utilisateur qui a créé le message
    private String message; // contenu du message
    private LocalDateTime created_at; // Date et heure de création du message
    private LocalDateTime updated_at; // Date de la dernière mise à jour du message


}
