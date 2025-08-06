package com.linreelle.saphir.service.services;

import com.linreelle.saphir.dto.services.BundleRequest;
import com.linreelle.saphir.model.User;
import com.linreelle.saphir.model.services.Bundle;
import com.linreelle.saphir.model.services.Offer;
import com.linreelle.saphir.repository.services.BundleRepository;
import com.linreelle.saphir.repository.services.OfferRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BundleService {

    private final BundleRepository bundleRepository;
    private final OfferRepository offerRepository;

    public Bundle createBundle(BundleRequest request, Set<Long> offerIds) {
        Set<Offer> offers = new HashSet<>(offerRepository.findAllById(offerIds));

        Bundle bundle = new Bundle();
        request.setOfferIds(offerIds);
        bundle.setName(request.getName());
        bundle.setDescription(request.getDescription());
        bundle.setOffers(offers);

        return bundleRepository.save(bundle);
    }
    public List<Bundle> getAllBundles() {
        return bundleRepository.findAll();
    }

    public Optional<Bundle> getBundleById(Long id) {
        return bundleRepository.findById(id);
    }


    @Transactional
    public Bundle updateBundle(Long id, Bundle updatedBundle, Set<Long> offerIds) {
        Bundle existingBundle = bundleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bundle not found with ID " + id));

        existingBundle.setName(updatedBundle.getName());

        Set<Offer> offers = new HashSet<>(offerRepository.findAllById(offerIds));
        existingBundle.getOffers().clear();
        existingBundle.getOffers().addAll(offers);

        return bundleRepository.save(existingBundle);
    }

    public void deleteBundle(Long id) {
        bundleRepository.deleteById(id);
    }
}
