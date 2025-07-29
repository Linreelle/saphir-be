package com.linreelle.saphir.repository.services;

import com.linreelle.saphir.model.services.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Integer> {
    boolean existsByNameAndIdNot(String name, Integer id);
    boolean existsByName (String name);
    List<Offer> findByIsActiveTrue();
}