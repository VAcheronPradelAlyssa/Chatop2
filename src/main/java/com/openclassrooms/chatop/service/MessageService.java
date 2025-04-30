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
     *
     * @param messageRequest la demande contenant les détails du message, l’ID d’utilisateur et l’ID de location
     * @return 
     */
    public MessageResponse createMessage(MessageRequest messageRequest) {
        Messages message = new Messages();

        // Récupérer l’utilisateur de la base de données en utilisant l’ID d’utilisateur de la requête
        Optional<User> userInDB = userRepository.findById(Integer.valueOf(messageRequest.getUser_id()));
        if (userInDB.isPresent()) {
            message.setUser(userInDB.get());
        } else {
            throw new RuntimeException("User not found");
        }

        // Récupérer la location à partir de la base de données en utilisant l’ID de location de la demande
        Optional<Rental> rentalInDB = rentalRepository.findById(messageRequest.getRental_id());
        if (rentalInDB.isPresent()) {
            message.setRental(rentalInDB.get());
        } else {
            throw new RuntimeException("Rental not found");
        }

        message.setMessage(messageRequest.getMessage());
        message.setCreatedAt(LocalDateTime.now());
        message.setUpdatedAt(LocalDateTime.now());

        // Enregistre dans la base de données
        messageRepository.save(message);

        // Retourne une reponse avec le contenu du message
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