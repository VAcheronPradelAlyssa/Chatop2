package com.openclassrooms.chatop.service;

import com.openclassrooms.chatop.dto.RentalDTO;
import com.openclassrooms.chatop.dto.RentalResponse;
import com.openclassrooms.chatop.entity.Rental;
import com.openclassrooms.chatop.entity.User;
import com.openclassrooms.chatop.mappers.RentalMapper;
import com.openclassrooms.chatop.repository.RentalRepository;
import com.openclassrooms.chatop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RentalService {

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads";

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RentalMapper rentalMapper;

    public List<RentalResponse> getAllRentals() {
        return rentalRepository.findAll()
                .stream().map(rentalMapper)
                .toList();
    }

    public Optional<RentalResponse> getRentalById(Integer id) {
        return rentalRepository.findById(id)
                .map(rentalMapper);
    }

    public Rental createRental(RentalDTO rentalDTO) {
        User owner = userRepository.findByEmail(rentalDTO.getOwnerEmail())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        Rental rental = new Rental();
        rental.setName(rentalDTO.getName());
        rental.setSurface(rentalDTO.getSurface());
        rental.setPrice(rentalDTO.getPrice());
        rental.setDescription(rentalDTO.getDescription());
        rental.setOwner(owner);
        rental.setCreatedAt(LocalDateTime.now());
        rental.setUpdatedAt(LocalDateTime.now());

        // Enregistrement du fichier image si présent
        if (rentalDTO.getPictureFile() != null && !rentalDTO.getPictureFile().isEmpty()) {
            String imagePath = savePicture(rentalDTO.getPictureFile());
            rental.setPicture(imagePath);
        } else {
            rental.setPicture(null);
        }

        return rentalRepository.save(rental);
    }

   

    private String savePicture(MultipartFile pictureFile) {
        try {
            // Création du dossier s'il n'existe pas
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = StringUtils.cleanPath(pictureFile.getOriginalFilename());
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFileName = UUID.randomUUID() + extension;
            Path filePath = uploadPath.resolve(newFileName);

            Files.copy(pictureFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Retourne le chemin relatif accessible par le front
            return "/uploads/" + newFileName;

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage(), e);
        }
    }
}
