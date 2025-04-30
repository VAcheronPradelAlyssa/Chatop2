package com.openclassrooms.chatop.repository;

import com.openclassrooms.chatop.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for the Message entity, providing CRUD operations and custom queries.
 */
public interface MessageRepository extends JpaRepository<Message, Integer> {

    /**
     * Finds a list of messages associated with a specific rental by rental ID.
     * @param rentalId the ID of the rental
     * @return a list of messages linked to the specified rental
     */
    List<Message> findByRentalId(Integer rentalId);

    /**
     * Finds a list of messages associated with a specific user by user ID.
     * @param userId the ID of the user
     * @return a list of messages linked to the specified user
     */
    List<Message> findByUserId(Integer userId);
}
