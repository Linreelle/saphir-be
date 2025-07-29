package com.linreelle.saphir.repository;

import com.linreelle.saphir.model.Confirmation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationRepository extends JpaRepository<Confirmation, Integer> {
    Confirmation findByToken(String token);

}
