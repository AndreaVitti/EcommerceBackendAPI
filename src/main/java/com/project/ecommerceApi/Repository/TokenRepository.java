package com.project.ecommerceApi.Repository;

import com.project.ecommerceApi.Entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query("""
            Select t from Token t
            inner join User u on
            t.user.id = u.id
            where t.user.id = :userId and t.logout = false
            """)
    List<Token> findTokenByUserId(Long userId);

    Optional<Token> findByJwtToken(String jwtToken);

    @Modifying
    @Query("""
            Delete from Token t where t.jwtToken = :jwt
            """)
    void deleteByJwtToken(String jwt);
}
