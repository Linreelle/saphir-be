package com.linreelle.saphir.repository.services;

import com.linreelle.saphir.model.services.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, Long> {
}
