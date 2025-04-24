package com.openclassrooms.chatop.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RentalDTO {
    private Integer id; // Identifiant de la location
    private String name;
    private Double surface;
    private Double price;
    private String picture; // Chemin vers l'image (stocké en base)
    private String description;
    private String ownerEmail; // Email du propriétaire

    // Fichier image à uploader (non persisté, pour la réception du fichier depuis le front)
    private transient MultipartFile pictureFile;
}
