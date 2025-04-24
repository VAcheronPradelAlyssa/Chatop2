package com.openclassrooms.chatop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RentalResponse {
    public RentalResponse(Long id2, String name2, Double surface2, Double price2, String picture2, String description2,
            Long id3) {
        //TODO Auto-generated constructor stub
    }
    private Integer id;
    private String name;
    private Double surface;
    private Double price;
    private String picture; // URL ou nom de l'image
    private String description;
    private Integer owner_id;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
