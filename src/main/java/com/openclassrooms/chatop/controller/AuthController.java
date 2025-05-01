package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.LoginResponseDto;
import com.openclassrooms.chatop.dto.LoginRequestDto;
import com.openclassrooms.chatop.dto.RegisterRequestDto;
import com.openclassrooms.chatop.dto.RegisterResponseDto;
import com.openclassrooms.chatop.dto.UserResponse;
import com.openclassrooms.chatop.entity.User;
import com.openclassrooms.chatop.repository.UserRepository;
import com.openclassrooms.chatop.service.JwtService;
import com.openclassrooms.chatop.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Contrôleur responsable de l'authentification des utilisateurs :
 * - Inscription (register)
 * - Connexion (login)
 * - Récupération des infos utilisateur connecté
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "API pour l'authentification des utilisateurs")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
 @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    /**
     * Retourne les informations de l'utilisateur actuellement connecté.
     *
     * @param token Le token JWT transmis dans l'en-tête Authorization
     * @return Les informations de l'utilisateur sous forme de UserResponse ou une erreur
     */
    @Operation(summary = "Get authenticated user info", description = "Retrieves information about the currently authenticated user.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User info retrieved successfully", content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized access"),
        @ApiResponse(responseCode = "403", description = "Forbidden access due to invalid token")
    })
    @GetMapping("/me")
    public ResponseEntity<?> getUserDetails(@RequestHeader("Authorization") String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token manquant ou invalide");
            }

            String jwtToken = token.substring(7); // Supprime "Bearer " du token
            if (!jwtService.isTokenValid(jwtToken)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token invalide");
            }

            String email = jwtService.extractEmailFromToken(jwtToken);
            Optional<User> userOptional = userRepository.findByEmail(email);

            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utilisateur non trouvé");
            }

            User user = userOptional.get();

            UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
            );

            return ResponseEntity.ok(userResponse);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur serveur");
        }
    }

    /**
     * Enregistre un nouvel utilisateur.
     *
     * @param request Données d'inscription (email, nom, mot de passe)
     * @return Un token JWT et l'identifiant de l'utilisateur, ou une erreur si l'email est déjà utilisé
     */
    @Operation(summary = "Register a new user", description = "Registers a new user and returns a JWT token.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User registered successfully", content = @Content(schema = @Schema(implementation = RegisterResponseDto.class))),
        @ApiResponse(responseCode = "409", description = "Email already in use")
    })
     @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody @Valid RegisterRequestDto request) {
        try {
            // Appeler le service pour enregistrer l'utilisateur et obtenir la réponse
            RegisterResponseDto response = userService.registerUser(request);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            // Si l'email est déjà pris, renvoyer une erreur de conflit
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    /**
     * Authentifie un utilisateur avec son email et mot de passe.
     *
     * @param loginRequest Données de connexion (email + mot de passe)
     * @return Un token JWT si les identifiants sont valides, sinon une erreur
     */
    @Operation(summary = "User login", description = "Authenticates a user with the provided credentials.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User authenticated successfully", content = @Content(schema = @Schema(implementation = LoginResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest) {
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utilisateur non trouvé");
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mot de passe invalide");
        }

        String token = jwtService.generateToken(user.getEmail());

        return ResponseEntity.ok(new LoginResponseDto(token, user.getId()));
    }
}
