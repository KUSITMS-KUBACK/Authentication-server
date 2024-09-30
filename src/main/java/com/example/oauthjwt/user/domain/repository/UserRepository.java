package com.example.oauthjwt.user.domain.repository;

import com.example.oauthjwt.user.domain.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findByUserId(UUID userId);
    Optional<User> findByProviderId(String providerId);
    void save(User user);
}