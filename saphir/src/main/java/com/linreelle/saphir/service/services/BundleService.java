package com.linreelle.saphir.service.services;

import com.linreelle.saphir.dto.services.BundleRequest;
import com.linreelle.saphir.model.services.Bundle;
import com.linreelle.saphir.model.services.Offer;
import com.linreelle.saphir.repository.services.BundleRepository;
import com.linreelle.saphir.repository.services.OfferRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BundleService {

    private final BundleRepository bundleRepository;
    private final OfferRepository offerRepository;

    public Bundle createBundle(BundleRequest request, Set<Long> offerIds, String createdBy) {
        if (offerIds == null || offerIds.isEmpty()) {
            throw new IllegalArgumentException("Offer IDs must be provided");
        }
        log.debug("Received offerIds: {}", offerIds);
        List<Offer> foundOffers = offerRepository.findAllById(offerIds);
        if (foundOffers.size() != offerIds.size()) {
            Set<Long> foundIds = foundOffers.stream()
                    .map(Offer::getId)
                    .collect(Collectors.toSet());
            Set<Long> missingIds = offerIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .collect(Collectors.toSet());
            throw new EntityNotFoundException("Missing offers with IDs: " + missingIds);
        }

        Set<Offer> validOffers = foundOffers.stream()
                .filter(offer -> offer.getId() != null && offer.getName() != null && !offer.getName().isBlank())
                .collect(Collectors.toSet());

        if (validOffers.size() != offerIds.size()) {
            throw new IllegalStateException("Some offers are invalid or incomplete");
        }



        Bundle bundle = new Bundle();
        bundle.setName(request.getName());
        bundle.setDescription(request.getDescription());
        //bundle.setOffers(new HashSet<>(foundOffers));
        bundle.setOffers(validOffers);
        bundle.setCreatedBy(createdBy);

        Bundle savedBundle = bundleRepository.save(bundle);

        // Optional: log or audit creation
        log.info("Bundle created: id={}, name={}, offers={}",
                savedBundle.getId(), savedBundle.getName(), offerIds);

        return savedBundle;
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
