package com.openclassrooms.chatop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDto {
    private String email;
    private String name;
    private String password;
}
