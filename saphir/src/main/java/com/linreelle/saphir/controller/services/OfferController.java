package com.linreelle.saphir.controller.services;

import com.linreelle.saphir.dto.services.OfferRequestDTO;
import com.linreelle.saphir.dto.services.OfferResponseDTO;
import com.linreelle.saphir.dto.validators.CreateOfferValidationGroup;
import com.linreelle.saphir.model.services.Subscription;
import com.linreelle.saphir.service.services.OfferService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/offers")
public class OfferController {
    private final OfferService offerService;

    @GetMapping
    @Operation(summary = "Get offers")
    public ResponseEntity<?> getOffers(){
        log.info("Fetching offers");
        System.out.println("Received request at /health");
        try {
            List<OfferResponseDTO> offers = offerService.getOffers();
            return ResponseEntity.ok().body(offers);
        } catch (Exception e) {
            log.error("error fetching offers", e);
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

        }
    }

    @PostMapping("/{offer_id}/subscribe")
    public ResponseEntity<String> subscribe(@PathVariable Integer offer_id, @RequestParam UUID user_id){
        offerService.subscribeUserToOffer(user_id, offer_id);
        return ResponseEntity.ok().body("success");
    }

    @GetMapping("/subscriptions/{userId}")
    public ResponseEntity<List<Subscription>> getUserSubscriptions(@PathVariable UUID userId) {
        List<Subscription> subscriptions = offerService.getUserSubscriptions(userId);
        return ResponseEntity.ok(subscriptions);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an offer")
    public ResponseEntity<OfferResponseDTO> getOffer(
            @PathVariable Integer id){
        OfferResponseDTO offer = offerService.getOffer(id);
        return ResponseEntity.ok().body(offer);
    }

    @PostMapping
    @Operation(summary = "Create a new offer")
    public ResponseEntity<OfferResponseDTO> createOffer(
            @Validated({Default.class, CreateOfferValidationGroup.class}) @RequestBody OfferRequestDTO offerRequestDTO){
        System.out.println("Received Offer: " + offerRequestDTO);

        OfferResponseDTO offerResponseDTO = offerService.createOffer(offerRequestDTO);
        return ResponseEntity.ok().body(offerResponseDTO);
    }
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing offer")
    public ResponseEntity<OfferResponseDTO> updateBundle(
            @PathVariable Integer id, @Validated({Default.class}) @RequestBody OfferRequestDTO offerRequestDTO){

        OfferResponseDTO offerResponseDTO = offerService.updateOffer(id, offerRequestDTO);
        return ResponseEntity.ok().body(offerResponseDTO);
    }
    @DeleteMapping("{id}")
    @Operation(summary = "Delete an offer")
    public ResponseEntity<Void> deleteOffer(@PathVariable Integer id){
        offerService.deleteOffer(id);
        return ResponseEntity.noContent().build();
    }


}

