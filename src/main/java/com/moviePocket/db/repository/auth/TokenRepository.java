package com.moviePocket.db.repository.auth;

import com.moviePocket.db.entities.auth.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    List<Token> findAllValidTokenByUser_Id(Long id);

    Optional<Token> findByToken(String token);
}
