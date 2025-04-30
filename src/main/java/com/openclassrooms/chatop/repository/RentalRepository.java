package com.openclassrooms.chatop.repository;

import com.openclassrooms.chatop.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface de dépôt pour l’entité Rental.
 * Fournit les opérations CRUD de base via JpaRepository.
 */
@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer> {
}
