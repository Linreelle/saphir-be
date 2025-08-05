package com.linreelle.saphir.service.services;

import com.linreelle.saphir.model.User;
import com.linreelle.saphir.model.services.Bundle;
import com.linreelle.saphir.model.services.Offer;
import com.linreelle.saphir.repository.UserRepository;
import com.linreelle.saphir.repository.services.BundleRepository;
import com.linreelle.saphir.repository.services.OfferRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final UserRepository userRepository;
    private final OfferRepository offerRepository;
    private final BundleRepository bundleRepository;


    public User subscribeToOffer(UUID userId, Long offerId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new EntityNotFoundException("Offer not found"));
        user.getSubscribedOffers().add(offer);
        return userRepository.save(user);
    }

    public User subscribeToBundle(UUID userId, Long bundleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Bundle bundle = bundleRepository.findById(bundleId)
                .orElseThrow(() -> new EntityNotFoundException("Bundle not found"));
        user.getSubscribedBundles().add(bundle);
        return userRepository.save(user);
    }

    public Set<Offer> getAllOffersForUser(User user) {
        Set<Offer> all = new HashSet<>(user.getSubscribedOffers());
        user.getSubscribedBundles().forEach(bundle -> all.addAll(bundle.getOffers()));
        return all;
    }
}
