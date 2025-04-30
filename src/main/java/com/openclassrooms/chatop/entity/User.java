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

@Entity
@Table(name = "USERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;  // Le mot de passe sera crypté avant de l'enregistrer

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        this.updatedAt = LocalDateTime.now();
    }
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    // Relation un-à-plusieurs avec les messages, traitement des opérations en cascade et suppression des orphelins

    private List<Messages> messages;

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();  // Aucune autorité/aucun rôle
    }

    @Override
    public String getUsername() {
        return this.email;  // Utilisation de l'email comme identifiant
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // L'utilisateur est toujours valide
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // L'utilisateur n'est pas bloqué
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // Les identifiants ne sont pas expirés
    }

    @Override
    public boolean isEnabled() {
        return true;  // L'utilisateur est activé
    }
}
