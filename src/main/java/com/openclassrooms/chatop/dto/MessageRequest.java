package com.openclassrooms.chatop.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {

    @NotNull(message = "Rental ID must be provided.")
    private Integer rental_id; // ID de la location associée au message, ne doit pas être null.

    @NotNull(message = "User ID must be provided.")
    private Integer user_id; // ID de l’utilisateur qui crée le message, ne doit pas être null.

    @Size(max = 2000 , message = "Message content must not exceed 2000 characters.")
    private String message; // Le contenu du message, limité à 2000 caractères.
}
