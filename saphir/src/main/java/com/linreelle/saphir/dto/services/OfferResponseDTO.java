package com.linreelle.saphir.dto.services;


import com.linreelle.saphir.model.services.Bundle;
import lombok.Data;

@Data

public class OfferResponseDTO {
    private String id;
    private String name;
    private String description;
    private Bundle bundle;
}
