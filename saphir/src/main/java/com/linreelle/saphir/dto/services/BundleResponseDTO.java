package com.linreelle.saphir.dto.services;

import com.linreelle.saphir.model.services.Offer;
import lombok.Data;

import java.util.List;

@Data
public class BundleResponseDTO {
    private String id;
    private String name;
    private String description;
    private List<Offer> offers;
}