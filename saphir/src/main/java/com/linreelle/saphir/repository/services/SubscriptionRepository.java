package com.linreelle.saphir.repository.services;

import com.linreelle.saphir.model.services.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
    List<Subscription> findByUserId(UUID userId);
}

