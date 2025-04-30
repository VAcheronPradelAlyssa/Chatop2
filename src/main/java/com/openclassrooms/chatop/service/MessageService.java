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
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RentalRepository rentalRepository;

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
        Messages message = new Messages();

        Optional<User> userInDB = userRepository.findById(Integer.valueOf(messageRequest.getUser_id()));
        if (userInDB.isPresent()) {
            message.setUser(userInDB.get());
        } else {
            throw new RuntimeException("User not found");
        }

        Optional<Rental> rentalInDB = rentalRepository.findById(messageRequest.getRental_id());
        if (rentalInDB.isPresent()) {
            message.setRental(rentalInDB.get());
        } else {
            throw new RuntimeException("Rental not found");
        }

        message.setMessage(messageRequest.getMessage());
        message.setCreatedAt(LocalDateTime.now());
        message.setUpdatedAt(LocalDateTime.now());

        messageRepository.save(message);

        return new MessageResponse(
                message.getId(),
                message.getRental().getId(),
                message.getUser().getId(),
                message.getMessage(),
                message.getCreatedAt(),
                message.getUpdatedAt()
        );
    }
}
