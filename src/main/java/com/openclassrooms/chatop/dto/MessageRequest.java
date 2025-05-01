package com.openclassrooms.chatop.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for handling message creation requests.
 * Validates input data for message creation related to a rental.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {

    @NotNull(message = "Rental ID must be provided.")
    private Integer rental_id; // ID of the rental associated with the message, must not be null.

    @NotNull(message = "User ID must be provided.")
    private Integer user_id; // ID of the user creating the message, must not be null.

    @Size(max = 2000, message = "Message content must not exceed 2000 characters.")
    private String message; // The message content, limited to 2000 characters.
}
