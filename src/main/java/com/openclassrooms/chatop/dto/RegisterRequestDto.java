package com.openclassrooms.chatop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDto {
       @NotBlank(message = "Name is required") // Ne doit pas être vide
    private String name;

    @NotBlank(message = "Email is required") // Ne doit pas être vide
    @Email(message = "Email should be valid") // Doit être un email valide
    private String email;

    @NotBlank(message = "Password is required") // Ne doit pas être vide
    private String password;
}
