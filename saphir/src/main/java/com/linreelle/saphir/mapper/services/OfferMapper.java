package com.linreelle.saphir.mapper.services;

import com.linreelle.saphir.dto.services.OfferRequestDTO;
import com.linreelle.saphir.dto.services.OfferResponseDTO;
import com.linreelle.saphir.model.services.Offer;

public class OfferMapper {
    public static OfferResponseDTO toDTO(Offer offer){
        OfferResponseDTO offerResponseDTO = new OfferResponseDTO();
        offerResponseDTO.setId(offer.getId().toString());
        offerResponseDTO.setName(offer.getName());
        offerResponseDTO.setDescription(offer.getDescription());

        return offerResponseDTO;
    }

    public static Offer toModel (OfferRequestDTO offerRequestDTO){
        Offer offer = new Offer();
        offer.setName(offerRequestDTO.getName());
        offer.setDescription(offerRequestDTO.getDescription());

        return offer;
    }
}
