package com.linreelle.saphir.controller.services;

import com.linreelle.saphir.model.User;
import com.linreelle.saphir.model.services.Offer;
import com.linreelle.saphir.service.services.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping("/offer/{offerId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> subscribeToOffer(@PathVariable Long offerId) {
        UUID userId = getAuthenticatedUserId();
        subscriptionService.subscribeToOffer(userId, offerId);
        return ResponseEntity.ok("Subscribed to offer successfully.");
    }

    @PostMapping("/bundle/{bundleId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> subscribeToBundle(@PathVariable Long bundleId) {
        UUID userId = getAuthenticatedUserId();
        subscriptionService.subscribeToBundle(userId, bundleId);
        return ResponseEntity.ok("Subscribed to bundle successfully.");
    }
    private UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) { // Your custom User class
            return ((User) principal).getId();
        }
        throw new RuntimeException("Unexpected principal type: " + principal.getClass());
    }
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    @GetMapping("/user")
    public Set<Offer> getUserOffers(User user) {
        return new HashSet<>(subscriptionService.getAllOffersForUser(user));
    }
}
