package com.linreelle.saphir.repository;

import com.linreelle.saphir.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User,UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByTelephone(String telephone);
    List<User> findByUser(boolean user);
    Page<User> findByUserTrue(Pageable pageable);


    User findByEmailIgnoreCase(String email);
    Boolean existsByEmail(String email);
    boolean existsByTelephone(String telephone);
    boolean existsByEmailAndIdNot(String email, UUID id);
    boolean existsByTelephoneAndIdNot(String telephone, UUID id);

    User findByProfile(String profile);
}
