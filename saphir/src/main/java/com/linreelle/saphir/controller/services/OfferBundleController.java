package com.linreelle.saphir.controller.services;

import com.linreelle.saphir.dto.services.BundleRequest;
import com.linreelle.saphir.model.User;
import com.linreelle.saphir.model.services.Bundle;
import com.linreelle.saphir.model.services.Offer;
import com.linreelle.saphir.service.services.BundleService;
import com.linreelle.saphir.service.services.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OfferBundleController {
    private final OfferService offerService;
    private final BundleService bundleService;

    private String getLoggedInUserName(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User){
            return ((User) principal).getFirstName() + " " + ((User) principal).getLastName();
        }
        return principal.toString();
    }

    // === Offer endpoints ===
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @PostMapping("/offers")
    public ResponseEntity<Offer> createOffer(@RequestBody Offer offer, ModelMap modelMap) {
        offer.setCreatedBy(getLoggedInUserName());
        Offer created = offerService.createOffer(offer);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    @GetMapping("/offers")
    public ResponseEntity<List<Offer>> getAllOffers() {
        List<Offer> offers = offerService.getAllOffers();
        return ResponseEntity.ok(offers);
    }

    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    @GetMapping("/offers/{id}")
    public ResponseEntity<Offer> getOfferById(@PathVariable Long id) {
        return offerService.getOfferById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @PutMapping("/offers/{id}")
    public ResponseEntity<Offer> updateOffer(@PathVariable Long id, @RequestBody Offer offer,  ModelMap modelMap) {
        offer.setUpdatedBy(getLoggedInUserName());
        Offer updated = offerService.updateOffer(id, offer);
        return ResponseEntity.ok(updated);
    }
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @DeleteMapping("/offers/{id}")
    public ResponseEntity<Void> deleteOffer(@PathVariable Long id) {
        offerService.deleteOffer(id);
        return ResponseEntity.noContent().build();
    }

    // === Bundle endpoints ===

    // Create bundle, expects JSON { "name": "...", "offerIds": [...] }
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @PostMapping("/bundles")
    public ResponseEntity<Bundle> createBundle(@RequestBody BundleRequest bundleRequest, ModelMap modelMap) {
        Bundle bundle = new Bundle();
        bundle.setName(bundleRequest.getName());
        bundle.setCreatedBy(getLoggedInUserName());
        Bundle created = bundleService.createBundle(bundle, bundleRequest.getOfferIds());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/bundles")
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public ResponseEntity<List<Bundle>> getAllBundles() {
        List<Bundle> bundles = bundleService.getAllBundles();
        return ResponseEntity.ok(bundles);
    }

    @GetMapping("/bundles/{id}")
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public ResponseEntity<Bundle> getBundleById(@PathVariable Long id) {
        return bundleService.getBundleById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update bundle, expects JSON { "name": "...", "offerIds": [...] }
    @PutMapping("/bundles/{id}")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<Bundle> updateBundle(@PathVariable Long id, @RequestBody BundleRequest bundleRequest, ModelMap modelMap) {
        Bundle updatedBundle = new Bundle();
        updatedBundle.setName(bundleRequest.getName());
        updatedBundle.setUpdatedBy(getLoggedInUserName());
        Bundle updated = bundleService.updateBundle(id, updatedBundle, bundleRequest.getOfferIds());
        return ResponseEntity.ok(updated);
    }
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @DeleteMapping("/bundles/{id}")
    public ResponseEntity<Void> deleteBundle(@PathVariable Long id) {
        bundleService.deleteBundle(id);
        return ResponseEntity.noContent().build();
    }

}
