package com.openclassrooms.chatop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration Spring MVC personnalisée.
 * Cette classe permet de définir des règles d'accès à des ressources statiques,
 * notamment les fichiers uploadés (images, etc.).
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Ajoute un gestionnaire de ressources pour exposer le dossier local "uploads"
     * à travers l'URL "/uploads/**".
     * Cela permet d'accéder aux fichiers via le navigateur comme si c'était une ressource web.
     *
     * Exemple : "uploads/image1.jpg" sera accessible à "http://localhost:3001/uploads/image1.jpg"
     *
     * @param registry le registre où ajouter les mappings de ressources
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
            .addResourceHandler("/uploads/**") // Chemin accessible depuis le navigateur
            .addResourceLocations("file:uploads/"); // Dossier physique dans le serveur (racine du projet)
    }
}
