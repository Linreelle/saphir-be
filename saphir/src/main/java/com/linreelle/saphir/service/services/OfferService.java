package com.linreelle.saphir.service.services;

import com.linreelle.saphir.dto.services.OfferDto;
import com.linreelle.saphir.mapper.services.OfferMapper;
import com.linreelle.saphir.model.services.Bundle;
import com.linreelle.saphir.model.services.Offer;
import com.linreelle.saphir.repository.services.OfferRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OfferService {
    private final OfferRepository offerRepository;


    public Offer createOffer(Offer offer) {
        return offerRepository.save(offer);
    }

    public List<OfferDto> getAllOffers() {
        List<Offer> offers = offerRepository.findAll();
        return offers.stream().map(OfferMapper::toDTO).toList();
    }

    public List<Offer> getOffersForBundle(Long bundleId) {
        return offerRepository.findOffersByBundleId(bundleId);
    }

    public Optional<Offer> getOfferById(Long id) {
        return offerRepository.findById(id);
    }

    @Transactional
    public Offer updateOffer(Long id, Offer updatedOffer) {
        Offer existingOffer = offerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Offer not found with ID " + id));

        existingOffer.setName(updatedOffer.getName());
        existingOffer.setDescription(updatedOffer.getDescription());

        return offerRepository.save(existingOffer);
    }

    public void deleteOffer(Long id) {
        Offer offer = offerRepository.findById(id)
                        .orElseThrow(()-> new EntityNotFoundException("Offer not found with ID " + id));
        for (Bundle bundle : offer.getBundles()) {
            bundle.getOffers().remove(offer);
        }
        offer.getBundles().clear();
        offerRepository.delete(offer);
    }

}
