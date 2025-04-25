package com.openclassrooms.chatop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.chatop.dto.RentalDTO;
import com.openclassrooms.chatop.dto.RentalResponse;
import com.openclassrooms.chatop.entity.Rental;
import com.openclassrooms.chatop.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    @Autowired
    private RentalService rentalService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Rental> createRental(
            @RequestParam("name") String name,
            @RequestParam("surface") Double surface,
            @RequestParam("price") Double price,
            @RequestParam("picture") MultipartFile picture,
            @RequestParam("description") String description,
            @AuthenticationPrincipal UserDetails userDetails) {

        String ownerEmail = userDetails.getUsername();

        // Création du dossier si inexistant
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Sauvegarde de l'image
        String fileName = picture.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR, fileName);
        try {
            Files.write(filePath, picture.getBytes());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        // Création de la DTO avec l'URL relative
        RentalDTO rentalDTO = new RentalDTO();
        rentalDTO.setName(name);
        rentalDTO.setSurface(surface);
        rentalDTO.setPrice(price);
        rentalDTO.setPicture("/uploads/" + fileName); // <- URL accessible via le navigateur
        rentalDTO.setDescription(description);
        rentalDTO.setOwnerEmail(ownerEmail);

        Rental createdRental = rentalService.createRental(rentalDTO);
        return ResponseEntity.ok(createdRental);
    }

    @GetMapping
    public Map<String, List<RentalResponse>> getRentals() {
        List<RentalResponse> rentalList = rentalService.getAllRentals();
        Map<String, List<RentalResponse>> response = new HashMap<>();
        response.put("rentals", rentalList);
        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalResponse> getRentalById(@PathVariable Integer id) {
        return rentalService.getRentalById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Rental> updateRental(
            @PathVariable Integer id,
            @RequestPart("rental") RentalDTO rentalDTO,
            @RequestPart(value = "picture", required = false) MultipartFile pictureFile
    ) {
        // Met à jour le DTO avec l'ID et le fichier image
        rentalDTO.setId(id);
        rentalDTO.setPictureFile(pictureFile);
        
        // Appeler le service pour la mise à jour
        Rental updated = rentalService.updateRental(rentalDTO);
        return ResponseEntity.ok(updated);
    }
    

}
