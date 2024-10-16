package com.csci318.auth.repository;

import com.csci318.auth.model.entity.TokenSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenSessionRepository extends JpaRepository<TokenSession, Long> {
    Optional<TokenSession> findByToken(String token);
    void deleteByToken(String token);
}
