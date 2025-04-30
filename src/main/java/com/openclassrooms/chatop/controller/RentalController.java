package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.RentalDto;
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

/**
 * Contrôleur REST pour la gestion des locations.
 */
@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private RentalService rentalService;

    /**
     * Crée une nouvelle location avec image (upload multipart).
     *
     * @param name Nom de la location
     * @param surface Surface en m²
     * @param price Prix de la location
     * @param picture Fichier image (optionnel)
     * @param description Description de la location
     * @param userDetails Utilisateur connecté (injecté par Spring Security)
     * @return L'objet Rental créé
     */
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Rental> createRental(
            @RequestParam("name") String name,
            @RequestParam("surface") Double surface,
            @RequestParam("price") Double price,
            @RequestParam("picture") MultipartFile picture,
            @RequestParam("description") String description,
            @AuthenticationPrincipal UserDetails userDetails) {

        String ownerEmail = userDetails.getUsername();

        // Crée le dossier d'uploads s'il n'existe pas
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

        // Construction du Dto à partir des données
        RentalDto rentalDto = new RentalDto();
        rentalDto.setName(name);
        rentalDto.setSurface(surface);
        rentalDto.setPrice(price);
        rentalDto.setPicture("http://localhost:3001/uploads/" + fileName); // URL d’accès
        rentalDto.setDescription(description);
        rentalDto.setOwnerEmail(ownerEmail);

        Rental createdRental = rentalService.createRental(rentalDto);
        return ResponseEntity.ok(createdRental);
    }

    /**
     * Récupère toutes les locations.
     *
     * @return Une carte contenant la liste des locations sous la clé "rentals"
     */
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

    /**
     * Récupère une location par son ID.
     *
     * @param id ID de la location
     * @return La location correspondante ou 404
     */
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

    /**
     * Met à jour une location existante.
     *
     * @param id ID de la location
     * @param name Nouveau nom
     * @param surface Nouvelle surface
     * @param price Nouveau prix
     * @param picture Nouvelle image (facultatif)
     * @param description Nouvelle description
     * @param userDetails Utilisateur connecté (propriétaire)
     * @return La location mise à jour ou 404
     */
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

        RentalDto rentalDto = new RentalDto();
        rentalDto.setId(id);
        rentalDto.setName(name);
        rentalDto.setSurface(surface);
        rentalDto.setPrice(price);
        rentalDto.setPicture(picture != null ? picture.getOriginalFilename() : null); // Image mise à jour si présente
        rentalDto.setDescription(description);
        rentalDto.setOwnerEmail(ownerEmail);

        Rental updatedRental = rentalService.updateRental(rentalDto);
        return updatedRental != null ? ResponseEntity.ok(updatedRental) : ResponseEntity.notFound().build();
    }
}
