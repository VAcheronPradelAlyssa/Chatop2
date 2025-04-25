package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.UserResponse;
import com.openclassrooms.chatop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(
        @PathVariable Integer id,  // Utilisation de Integer au lieu de Long
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        // Vérification de la connexion de l'utilisateur
        if (userDetails == null) {
            return ResponseEntity.status(403).body("Unauthorized");
        }
    
        // Récupération de l'utilisateur par ID
        UserResponse user = userService.getUserResponseById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
    
        return ResponseEntity.ok(user);
    }
}
