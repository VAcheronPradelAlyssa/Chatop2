package com.openclassrooms.chatop.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PictureService {

    private static final String UPLOAD_DIR = "uploads"; // dossier dans ton projet ou en dehors

    public String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Le fichier est vide");
        }

        // Crée le dossier s’il n'existe pas
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Sauvegarde le fichier avec un nom unique
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR, filename);
        Files.copy(file.getInputStream(), filePath);

        // Tu peux ici retourner juste le nom ou une URL (ex : "/uploads/nom.png")
        return filename;
    }
}
