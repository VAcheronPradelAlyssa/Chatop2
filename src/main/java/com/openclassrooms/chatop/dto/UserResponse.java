package com.openclassrooms.chatop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * DTO for user response data, used to return user details in the response.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Integer id; // The unique identifier of the user.
    private String name; // The name of the user.
    private String email; // The email address of the user.
    private LocalDateTime created_at; // Timestamp of when the user was created.
    private LocalDateTime updated_at; // Timestamp of the last update to the user's details.
}
