package com.linreelle.saphir.repository.services;

import com.linreelle.saphir.model.services.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    @Query("SELECT o FROM Offer o JOIN o.bundles b WHERE b.id = :bundleId")
    List<Offer> findOffersByBundleId(@Param("bundleId") Long bundleId);
}
