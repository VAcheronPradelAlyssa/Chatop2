package com.openclassrooms.chatop.mappers;

import com.openclassrooms.chatop.dto.RentalResponse;
import com.openclassrooms.chatop.entity.Rental;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * Mapper permettant de transformer une entité Rental en DTO RentalResponse.
 * Utilisé pour renvoyer les données d’une location sous forme de réponse JSON.
 */
@Component
public class RentalMapper implements Function<Rental, RentalResponse> {

    @Override
    public RentalResponse apply(Rental rental) {
        return new RentalResponse(
                rental.getId(),
                rental.getName(),
                rental.getSurface(),
                rental.getPrice(),
                rental.getPicture(),
                rental.getDescription(),
                rental.getOwner() != null ? rental.getOwner().getId() : null,
                rental.getCreatedAt(),
                rental.getUpdatedAt()
        );
    }
}
