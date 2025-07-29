package com.linreelle.saphir.mapper.services;

import com.linreelle.saphir.dto.services.BundleResponseDTO;
import com.linreelle.saphir.dto.services.CreateBundleRequestDTO;
import com.linreelle.saphir.model.services.Bundle;

public class BundleMapper {
    public static BundleResponseDTO toDTO(Bundle bundle){
        BundleResponseDTO bundleResponseDTO = new BundleResponseDTO();

        bundleResponseDTO.setId(bundle.getId().toString());
        bundleResponseDTO.setName(bundle.getName());
        bundleResponseDTO.setDescription(bundle.getDescription());
        bundleResponseDTO.setOffers(bundle.getOffers());

        return bundleResponseDTO;
    }

    public static Bundle toModel (CreateBundleRequestDTO createBundleRequestDTO){
        Bundle bundle = new Bundle();
        bundle.setName(createBundleRequestDTO.getName());
        bundle.setDescription(createBundleRequestDTO.getDescription());
        bundle.setOffers(createBundleRequestDTO.getOffers());

        return bundle;
    }
}

