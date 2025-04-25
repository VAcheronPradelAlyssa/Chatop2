package com.openclassrooms.chatop.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RentalDetailsDto {
    private Integer id;
    private String name;
    private Double surface;
    private Double price;
    private String picture;
    private String description;

    private Integer owner_id;
    private String owner_name;
    private String owner_email;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
