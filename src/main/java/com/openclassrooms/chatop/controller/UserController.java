package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.UserResponse;
import com.openclassrooms.chatop.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour la récupération d'informations utilisateur.
 */
@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "API pour la récupération d'informations utilisateur")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Récupère les informations d'un utilisateur via son ID.
     *
     * @param id ID de l'utilisateur
     * @param userDetails Détails de l'utilisateur authentifié
     * @return Réponse contenant les données de l'utilisateur ou une erreur
     */
    @Operation(summary = "Get user by ID", description = "Fetches user information by ID.", security = {
            @SecurityRequirement(name = "Bearer Authentication") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Vérifie que l'utilisateur est authentifié
        if (userDetails == null) {
            return ResponseEntity.status(401).body("Unauthorized: You must be logged in.");
        }

        // Récupération de l'utilisateur par ID
        UserResponse user = userService.getUserResponseById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }
}
