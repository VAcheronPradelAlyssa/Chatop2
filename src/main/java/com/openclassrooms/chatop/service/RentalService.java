package com.openclassrooms.chatop.service;

import com.openclassrooms.chatop.dto.RentalDto;
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

/**
 * Service pour gérer les locations (rentals), incluant les opérations CRUD pour les locations.
 */
@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RentalMapper rentalMapper;

    /**
     * Récupère toutes les locations dans la base de données et les retourne sous forme de Dtos.
     * 
     * @return une liste de {@link RentalResponse} représentant les locations.
     */
    public List<RentalResponse> getAllRentals() {
        return rentalRepository.findAll()
                .stream()
                .map(rentalMapper)  // On transforme chaque Rental en RentalResponse
                .toList();  // Collecte la liste et la retourne
    }

    /**
     * Récupère une location par son ID.
     *
     * @param id L'ID de la location.
     * @return Un {@link Optional} contenant la {@link RentalResponse} si la location est trouvée, sinon un Optional vide.
     */
    public Optional<RentalResponse> getRentalById(Integer id) {
        return rentalRepository.findById(id)
                .map(rentalMapper);  // Conversion de Rental en RentalResponse si trouvé
    }

    /**
     * Crée une nouvelle location dans la base de données.
     * 
     * @param rentalDto les données de la location à créer.
     * @return l'objet {@link Rental} créé.
     * @throws RuntimeException si le propriétaire (owner) de la location n'est pas trouvé.
     */
    public Rental createRental(RentalDto rentalDto) {
        // Récupère l'utilisateur propriétaire de la location par son email
        User owner = userRepository.findByEmail(rentalDto.getOwnerEmail())
                                   .orElseThrow(() -> new RuntimeException("Owner not found"));

        // Création d'une nouvelle location
        Rental rental = new Rental();
        rental.setName(rentalDto.getName());
        rental.setSurface(rentalDto.getSurface());
        rental.setPrice(rentalDto.getPrice());
        rental.setPicture(rentalDto.getPicture());  // Chemin de l'image
        rental.setDescription(rentalDto.getDescription());
        rental.setOwner(owner);  // Définir le propriétaire de la location
        rental.setCreatedAt(LocalDateTime.now());
        rental.setUpdatedAt(LocalDateTime.now());

        // Sauvegarde la location dans la base de données
        return rentalRepository.save(rental);
    }

    /**
     * Met à jour une location existante dans la base de données.
     * 
     * @param rentalDto les données de la location à mettre à jour.
     * @return l'objet {@link Rental} mis à jour.
     * @throws RuntimeException si la location à mettre à jour n'est pas trouvée.
     */
    public Rental updateRental(RentalDto rentalDto) {
        // Récupère la location existante à partir de son ID
        Rental rental = rentalRepository.findById(rentalDto.getId())
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        // Récupère l'utilisateur propriétaire de la location
        User owner = userRepository.findByEmail(rentalDto.getOwnerEmail())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        // Mise à jour des champs de la location
        rental.setName(rentalDto.getName());
        rental.setSurface(rentalDto.getSurface());
        rental.setPrice(rentalDto.getPrice());
        rental.setPicture(rentalDto.getPicture() != null ? rentalDto.getPicture() : rental.getPicture());  // Mise à jour de l'image si un nouveau fichier est fourni
        rental.setDescription(rentalDto.getDescription());
        rental.setOwner(owner);  // Le propriétaire reste le même
        rental.setUpdatedAt(LocalDateTime.now());

        // Sauvegarde la location mise à jour
        return rentalRepository.save(rental);
    }
}
