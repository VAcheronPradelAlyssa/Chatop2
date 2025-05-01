package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.RentalDto;
import com.openclassrooms.chatop.dto.RentalResponse;
import com.openclassrooms.chatop.entity.Rental;
import com.openclassrooms.chatop.service.PictureService;
import com.openclassrooms.chatop.service.RentalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rentals")
@Tag(name = "Rentals", description = "API pour la gestion des locations")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @Autowired
    private PictureService pictureService;

    @Operation(summary = "Créer une nouvelle location", description = "Crée une nouvelle location avec une image.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Location créée avec succès"),
        @ApiResponse(responseCode = "401", description = "Accès non autorisé")
    })
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Map<String, String>> createRental(
            @RequestParam("name") String name,
            @RequestParam("surface") Double surface,
            @RequestParam("price") Double price,
            @RequestParam("picture") MultipartFile picture,
            @RequestParam("description") String description,
            @AuthenticationPrincipal UserDetails userDetails) {

        String ownerEmail = userDetails.getUsername();

        String fileName;
        try {
            fileName = pictureService.uploadPicture(picture);
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Échec de l'upload de l'image."));
        }

        RentalDto rentalDto = new RentalDto();
        rentalDto.setName(name);
        rentalDto.setSurface(surface);
        rentalDto.setPrice(price);
        rentalDto.setPicture("http://localhost:3001/uploads/" + fileName);
        rentalDto.setDescription(description);
        rentalDto.setOwnerEmail(ownerEmail);

        rentalService.createRental(rentalDto);

        return ResponseEntity.ok(Map.of("message", "Location créée avec succès !"));
    }

    @Operation(summary = "Récupérer toutes les locations", description = "Récupère la liste de toutes les locations.", security = {
            @SecurityRequirement(name = "Bearer Authentication") })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des locations")
    })
    @GetMapping
    public Map<String, List<RentalResponse>> getRentals() {
        List<RentalResponse> rentalList = rentalService.getAllRentals();
        Map<String, List<RentalResponse>> response = new HashMap<>();
        response.put("rentals", rentalList);
        return response;
    }

    @Operation(summary = "Récupérer une location par ID", description = "Récupère les informations d'une location spécifique par son ID.", security = {
            @SecurityRequirement(name = "Bearer Authentication") })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Location trouvée"),
        @ApiResponse(responseCode = "404", description = "Location non trouvée")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RentalResponse> getRentalById(@PathVariable Integer id) {
        return rentalService.getRentalById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateRental(
            @PathVariable Integer id,
            @RequestParam("name") String name,
            @RequestParam("surface") Double surface,
            @RequestParam("price") Double price,
            @RequestParam(value = "picture", required = false) MultipartFile picture,
            @RequestParam("description") String description,
            @AuthenticationPrincipal UserDetails userDetails) {

        String ownerEmail = userDetails.getUsername();

        String fileName = null;
        if (picture != null && !picture.isEmpty()) {
            try {
                fileName = pictureService.uploadPicture(picture);
            } catch (IOException e) {
                return ResponseEntity.internalServerError()
                        .body(Map.of("message", "Échec de l'upload de l'image."));
            }
        }

        RentalDto rentalDto = new RentalDto();
        rentalDto.setId(id);
        rentalDto.setName(name);
        rentalDto.setSurface(surface);
        rentalDto.setPrice(price);
        rentalDto.setDescription(description);
        rentalDto.setOwnerEmail(ownerEmail);
        if (fileName != null) {
            rentalDto.setPicture("http://localhost:3001/uploads/" + fileName);
        }

        Rental updatedRental = rentalService.updateRental(rentalDto);

        if (updatedRental != null) {
            return ResponseEntity.ok(Map.of("message", "Location mise à jour avec succès !"));
        } else {
            return ResponseEntity.status(404).body(Map.of("message", "Location non trouvée !"));
        }
    }
}
