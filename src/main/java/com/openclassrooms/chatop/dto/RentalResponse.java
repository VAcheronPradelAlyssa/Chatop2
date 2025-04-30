package com.openclassrooms.chatop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/**
 * DTO pour renvoyer les informations d'une location.
 * Ce DTO contient les détails d'une location, y compris son nom, sa surface, son prix, etc.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RentalResponse {
    private Integer id;
    private String name;
    private Double surface;
    private Double price;
    private String picture;
    private String description;
    private Integer owner_id;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}