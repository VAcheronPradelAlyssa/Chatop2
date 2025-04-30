package com.openclassrooms.chatop.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * DTO utilisé pour la création ou la mise à jour d'une location.
 * Ce DTO inclut les informations de base pour une location ainsi qu'un fichier image.
 */
@Data
public class RentalRequest {
    private String name;
    private Double surface;
    private Double price;
    private String description;
    private MultipartFile picture; 
}
