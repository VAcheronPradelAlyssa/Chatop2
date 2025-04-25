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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RentalMapper rentalMapper;
   /**
     * Récupère toutes les locations dans la base de données.
     *
     * @return la liste des rentals sous forme de DTOs
     */
    public List<RentalResponse> getAllRentals() {
        return rentalRepository.findAll()
                .stream().map(rentalMapper)  // On transforme chaque Rental en RentalResponse
                .toList(); // Collecte la liste
    }

    /**
     * Récupère une location par son ID.
     *
     * @param id L'ID de la location.
     * @return Un Optional contenant la RentalResponse si la location est trouvée, sinon un Optional vide.
     */
    public Optional<RentalResponse> getRentalById(Integer id) {
        return rentalRepository.findById(id)
                .map(rentalMapper);  // Conversion de Rental en RentalResponse si trouvé
    }

    public Rental createRental(RentalDTO rentalDTO) {
        // Récupère l'utilisateur par son email
        User owner = userRepository.findByEmail(rentalDTO.getOwnerEmail())
                                   .orElseThrow(() -> new RuntimeException("Owner not found"));

        Rental rental = new Rental();
        rental.setName(rentalDTO.getName());
        rental.setSurface(rentalDTO.getSurface());
        rental.setPrice(rentalDTO.getPrice());
        rental.setPicture(rentalDTO.getPicture()); // Chemin de l'image
        rental.setDescription(rentalDTO.getDescription());
        rental.setOwner(owner); // Définir l'utilisateur propriétaire
        rental.setCreatedAt(LocalDateTime.now());
        rental.setUpdatedAt(LocalDateTime.now());

        return rentalRepository.save(rental);
    }

    public Rental updateRental(RentalDTO rentalDTO) {
        Rental rental = rentalRepository.findById(rentalDTO.getId())
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        User owner = userRepository.findByEmail(rentalDTO.getOwnerEmail())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        rental.setName(rentalDTO.getName());
        rental.setSurface(rentalDTO.getSurface());
        rental.setPrice(rentalDTO.getPrice());
        rental.setPicture(rentalDTO.getPicture() != null ? rentalDTO.getPicture() : rental.getPicture()); // Met à jour l'image si un nouveau fichier est fourni
        rental.setDescription(rentalDTO.getDescription());
        rental.setOwner(owner); // Le propriétaire reste le même
        rental.setUpdatedAt(LocalDateTime.now());

        return rentalRepository.save(rental);
    }

  

    
}
