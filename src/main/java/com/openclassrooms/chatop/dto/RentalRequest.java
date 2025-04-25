package com.openclassrooms.chatop.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RentalRequest {
    private String name;
    private Double surface;
    private Double price;
    private String description;
    private MultipartFile picture; // pour le fichier uploadé
}
