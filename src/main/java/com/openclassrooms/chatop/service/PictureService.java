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

    private static final String UPLOAD_DIR = "uploads/";

    public String uploadPicture(MultipartFile picture) throws IOException {
        // Vérifiez que le fichier n'est pas vide et a un nom
        if (picture.isEmpty() || picture.getOriginalFilename() == null) {
            throw new IllegalArgumentException("Le fichier est vide ou n'a pas de nom.");
        }

        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String originalFilename = picture.getOriginalFilename();

        // Vérifiez si le nom de fichier contient un point pour extraire l'extension
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex < 0) {
            throw new IllegalArgumentException("Le nom de fichier n'a pas d'extension valide.");
        }

        String fileBaseName = originalFilename.substring(0, dotIndex);
        String fileExtension = originalFilename.substring(dotIndex);

        // Utilisez le nom de base original et ajoutez un timestamp pour garantir l'unicité
        String fileName = fileBaseName + "_" + System.currentTimeMillis() + fileExtension;

        Path filePath = Paths.get(UPLOAD_DIR, fileName);
        Files.write(filePath, picture.getBytes());

        return fileName;
    }
}
