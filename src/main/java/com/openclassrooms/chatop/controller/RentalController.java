package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.RentalDTO;
import com.openclassrooms.chatop.dto.RentalDetailsDto;
import com.openclassrooms.chatop.repository.RentalRepository;

import com.openclassrooms.chatop.dto.RentalResponse;
import com.openclassrooms.chatop.entity.Rental;
import com.openclassrooms.chatop.repository.RentalRepository;
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

    @Operation(summary = "Create a new rental", description = "Creates a new rental with the provided details.", security = {
            @SecurityRequirement(name = "Bearer Authentication") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rental created successfully", content = @Content(schema = @Schema(implementation = RentalResponse.class)))
    })
    @GetMapping
    public Map<String, List<RentalResponse>> getRentals() {
        List<RentalResponse> rentalList = rentalService.getAllRentals();
        Map<String, List<RentalResponse>> response = new HashMap<>();
        response.put("rentals", rentalList);
        return response;
    }

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


    @Operation(summary = "Update rental by ID", description = "Updates the information of a specific rental by its ID.", security = {
            @SecurityRequirement(name = "Bearer Authentication") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rental updated successfully", content = @Content(schema = @Schema(implementation = RentalResponse.class))),
            @ApiResponse(responseCode = "404", description = "Rental not found")
    })
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Rental> updateRental(
            @PathVariable Integer id,
            @RequestPart("rental") RentalDTO rentalDTO,
            @RequestPart(value = "picture", required = false) MultipartFile pictureFile) {
        // Met à jour le DTO avec l'ID et le fichier image
        rentalDTO.setId(id);
        rentalDTO.setPictureFile(pictureFile);

        // Appeler le service pour la mise à jour
        Rental updated = rentalService.updateRental(rentalDTO);
        return ResponseEntity.ok(updated);
    }

}
