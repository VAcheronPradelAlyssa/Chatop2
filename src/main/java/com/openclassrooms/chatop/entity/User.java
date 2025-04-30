package com.openclassrooms.chatop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Entité représentant un utilisateur de l'application.
 * Implémente l'interface UserDetails pour l'intégration avec Spring Security.
 */
@Entity
@Table(name = "USERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    /**
     * Identifiant unique de l'utilisateur (clé primaire).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Integer id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String password;
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Liste des messages envoyés par cet utilisateur.
     * Relation un-à-plusieurs avec cascade des opérations et suppression automatique des orphelins.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Messages> messages;

    /**
     * Initialise les dates lors de la création de l'entité.
     */
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Met à jour la date de modification avant chaque mise à jour de l'entité.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Méthodes obligatoires pour Spring Security (UserDetails)

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(); // Aucun rôle n’est attribué pour l’instant
    }

    @Override
    public String getUsername() {
        return this.email; // L’email est utilisé comme identifiant
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Le compte n’expire jamais
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Le compte n’est jamais bloqué
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Les identifiants sont toujours valides
    }

    @Override
    public boolean isEnabled() {
        return true; // L’utilisateur est actif
    }
}
