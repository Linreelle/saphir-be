package com.linreelle.saphir.repository;

import com.linreelle.saphir.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    @Query(value = """
        select t from Token t inner join User u\s
        on t.user.id = u.id\s where u.id = :id and (t.expired = false or t.revoked = false \s)
""")
    List<Token> findAllValidTokenByUser(UUID id);
    Optional<Token> findByToken(String token);

}
