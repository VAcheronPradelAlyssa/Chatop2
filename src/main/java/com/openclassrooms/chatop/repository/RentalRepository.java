package com.openclassrooms.chatop.repository;

import com.openclassrooms.chatop.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer> {
    // Pas besoin d'ajouter findById, c'est déjà inclus via JpaRepository
}
