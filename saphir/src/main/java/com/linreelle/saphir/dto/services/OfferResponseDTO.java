package com.linreelle.saphir.dto.services;


import com.linreelle.saphir.model.services.Bundle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferResponseDTO {
    private String id;
    private String name;
    private String description;
}
