package com.openclassrooms.chatop.service;

import com.openclassrooms.chatop.dto.MessageRequest;
import com.openclassrooms.chatop.dto.MessageResponse;
import com.openclassrooms.chatop.entity.Messages;
import com.openclassrooms.chatop.entity.Rental;
import com.openclassrooms.chatop.entity.User;
import com.openclassrooms.chatop.repository.MessageRepository;
import com.openclassrooms.chatop.repository.RentalRepository;
import com.openclassrooms.chatop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service de gestion des messages, incluant la création et la persistance des messages associés à une location et un utilisateur.
 */
@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;  // Repository pour l'entité Message

    @Autowired
    private UserRepository userRepository;  // Repository pour l'entité User

    @Autowired
    private RentalRepository rentalRepository;  // Repository pour l'entité Rental

    /**
     * Crée un nouveau message associé à un utilisateur et une location.
     * Cette méthode vérifie que l'utilisateur et la location existent dans la base de données,
     * puis enregistre le message dans la base de données.
     *
     * @param messageRequest la demande contenant les détails du message, l’ID d’utilisateur et l’ID de location
     * @return un objet MessageResponse contenant les informations du message créé
     * @throws RuntimeException si l'utilisateur ou la location n'existent pas dans la base de données
     */
    public MessageResponse createMessage(MessageRequest messageRequest) {
        // Création d'un nouvel objet Message
        Messages message = new Messages();

        // Récupérer l'utilisateur de la base de données en utilisant l'ID de l'utilisateur
        Optional<User> userInDB = userRepository.findById(Integer.valueOf(messageRequest.getUser_id()));
        if (userInDB.isPresent()) {
            message.setUser(userInDB.get());  // Lier l'utilisateur trouvé au message
        } else {
            throw new RuntimeException("User not found");  // Si l'utilisateur n'existe pas, une exception est levée
        }

        // Récupérer la location de la base de données en utilisant l'ID de la location
        Optional<Rental> rentalInDB = rentalRepository.findById(messageRequest.getRental_id());
        if (rentalInDB.isPresent()) {
            message.setRental(rentalInDB.get());  // Lier la location trouvée au message
        } else {
            throw new RuntimeException("Rental not found");  // Si la location n'existe pas, une exception est levée
        }

        // Définir le message, la date de création et la date de mise à jour
        message.setMessage(messageRequest.getMessage());
        message.setCreatedAt(LocalDateTime.now());  // Date de création
        message.setUpdatedAt(LocalDateTime.now());  // Date de mise à jour

        // Enregistrer le message dans la base de données
        messageRepository.save(message);

        // Retourner une réponse contenant les informations du message
        return new MessageResponse(
                message.getId(),  // ID du message
                message.getRental().getId(),  // ID de la location associée
                message.getUser().getId(),  // ID de l'utilisateur associé
                message.getMessage(),  // Contenu du message
                message.getCreatedAt(),  // Date de création
                message.getUpdatedAt()   // Date de mise à jour
        );
    }
}
