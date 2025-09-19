package com.linreelle.saphir.mapper.services;

import com.linreelle.saphir.dto.services.OfferDto;
import com.linreelle.saphir.model.services.Offer;

public class OfferMapper {
    public static OfferDto toDTO(Offer offer){
        OfferDto response = new OfferDto();

        response.setId(offer.getId());
        response.setName(offer.getName());
        response.setDescription(offer.getDescription());
        response.setOfferType(offer.getOfferType());

        return response;
    }
}
