package com.openclassrooms.chatop.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Service de gestion des images, incluant le téléchargement et la sauvegarde des fichiers.
 */
@Service
public class PictureService {

    // Dossier où les fichiers seront enregistrés
    private static final String UPLOAD_DIR = "uploads";  // dossier dans ton projet ou en dehors

    /**
     * Télécharge un fichier et le sauvegarde dans un répertoire spécifique avec un nom unique.
     * Si le fichier est vide, une exception est levée.
     *
     * @param file le fichier à télécharger
     * @return le nom du fichier sauvegardé
     * @throws IOException si une erreur se produit lors de l'écriture du fichier
     */
    public String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Le fichier est vide");  // Vérifie si le fichier est vide et lève une exception si nécessaire
        }

        // Crée le dossier de destination si nécessaire
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();  // Crée le répertoire si il n'existe pas
        }

        // Crée un nom de fichier unique basé sur le timestamp actuel et le nom original du fichier
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR, filename);

        // Sauvegarde le fichier sur le système de fichiers
        Files.copy(file.getInputStream(), filePath);

        // Retourne le nom du fichier sauvegardé (tu peux aussi retourner une URL si nécessaire)
        return filename;
    }
}
