package com.cyfrahub.backend.domain.repository;

import com.cyfrahub.backend.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByTelegramChatId(Long telegramChatId);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
