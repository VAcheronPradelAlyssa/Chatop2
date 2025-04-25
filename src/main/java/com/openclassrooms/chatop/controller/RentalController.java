package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.RentalDTO;
import com.openclassrooms.chatop.dto.RentalResponse;
import com.openclassrooms.chatop.entity.Rental;
import com.openclassrooms.chatop.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

        private static final String UPLOAD_DIR = "uploads/";


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

        // Création du dossier d'uploads si il n'existe pas
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Sauvegarder l'image dans le dossier
        String fileName = picture.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR, fileName);
        try {
            Files.write(filePath, picture.getBytes());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        // Créer le DTO avec le chemin de l'image
        RentalDTO rentalDTO = new RentalDTO();
        rentalDTO.setName(name);
        rentalDTO.setSurface(surface);
        rentalDTO.setPrice(price);
        rentalDTO.setPicture("http://localhost:3001/uploads/" + fileName); // URL complète pour accéder à l'image
        rentalDTO.setDescription(description);
        rentalDTO.setOwnerEmail(ownerEmail);

        // Créer la location et la sauvegarder dans la base de données
        Rental createdRental = rentalService.createRental(rentalDTO);
        return ResponseEntity.ok(createdRental);
    }

    // Endpoint pour récupérer toutes les locations
    @Operation(summary = "Get all rentals", description = "Fetches all rental listings.", security = {
            @SecurityRequirement(name = "Bearer Authentication") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of rentals", content = @Content(schema = @Schema(implementation = RentalResponse.class)))
    })
    @GetMapping
    public Map<String, List<RentalResponse>> getRentals() {
        List<RentalResponse> rentalList = rentalService.getAllRentals();
        Map<String, List<RentalResponse>> response = new HashMap<>();
        response.put("rentals", rentalList);
        return response;
    }

    // Endpoint pour récupérer une location spécifique par son ID
    @Operation(summary = "Get rental by ID", description = "Fetches rental information for a specific rental by its ID.", security = {
            @SecurityRequirement(name = "Bearer Authentication") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rental found", content = @Content(schema = @Schema(implementation = RentalResponse.class))),
            @ApiResponse(responseCode = "404", description = "Rental not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RentalResponse> getRentalById(@PathVariable Integer id) {
        return rentalService.getRentalById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint de mise à jour d'une location
    @Operation(summary = "Update rental by ID", description = "Updates the information of a specific rental by its ID.", security = {
            @SecurityRequirement(name = "Bearer Authentication") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rental updated successfully", content = @Content(schema = @Schema(implementation = RentalResponse.class))),
            @ApiResponse(responseCode = "404", description = "Rental not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Rental> updateRental(
            @PathVariable Integer id,
            @RequestParam("name") String name,
            @RequestParam("surface") Double surface,
            @RequestParam("price") Double price,
            @RequestParam(value = "picture", required = false) MultipartFile picture,
            @RequestParam("description") String description,
            @AuthenticationPrincipal UserDetails userDetails) {

        String ownerEmail = userDetails.getUsername();

        RentalDTO rentalDTO = new RentalDTO();
        rentalDTO.setId(id);
        rentalDTO.setName(name);
        rentalDTO.setSurface(surface);
        rentalDTO.setPrice(price);
        rentalDTO.setPicture(picture != null ? picture.getOriginalFilename() : null); // Prendre le nom du fichier si présent
        rentalDTO.setDescription(description);
        rentalDTO.setOwnerEmail(ownerEmail);

        Rental updatedRental = rentalService.updateRental(rentalDTO);
        return updatedRental != null ? ResponseEntity.ok(updatedRental) : ResponseEntity.notFound().build();
    }
}
