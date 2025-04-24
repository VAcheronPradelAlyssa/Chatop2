package com.openclassrooms.chatop.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String email;
    private String name;
    private String password;
}