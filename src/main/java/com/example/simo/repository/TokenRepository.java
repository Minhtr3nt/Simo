package com.example.simo.repository;

import com.example.simo.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, String> {
    Optional<Token> findByToken(String token);

    List<Token> findByUser_UserName(String userName);
}
