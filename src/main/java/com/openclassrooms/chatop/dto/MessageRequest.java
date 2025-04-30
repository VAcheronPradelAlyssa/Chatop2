package com.openclassrooms.chatop.dto;

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
public class MessageRequest {

    /**
     * L'ID de la location associée au message.
     * Ce champ ne doit pas être nul.
     */
    @NotNull(message = "Rental ID must be provided.")
    private Integer rental_id; // ID de la location associée au message, ne doit pas être null.

    /**
     * L'ID de l'utilisateur qui crée le message.
     * Ce champ ne doit pas être nul.
     */
    @NotNull(message = "User ID must be provided.")
    private Integer user_id; // ID de l’utilisateur qui crée le message, ne doit pas être null.

    /**
     * Le contenu du message.
     * Il est limité à 2000 caractères pour éviter des messages trop longs.
     */
    @Size(max = 2000 , message = "Message content must not exceed 2000 characters.")
    private String message; // Le contenu du message, limité à 2000 caractères.
}
