package com.linreelle.saphir.dto.services;

import com.linreelle.saphir.model.services.OfferType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferDto {
    private Long id;
    private String name;
    private String description;
    private OfferType offerType;
}
