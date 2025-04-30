package com.openclassrooms.chatop.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO contenant les détails d'une location.
 * Inclut les informations de base ainsi que les détails du propriétaire.
 */

@Data
public class RentalDto {
    private Integer id;
    private String name;
    private Double surface;
    private Double price;
    private String picture;
    private String description;
    private Integer owner_id;
    private String owner_name;
    private String ownerEmail; 
    private LocalDateTime updated_at;
    private LocalDateTime created_at;

}
