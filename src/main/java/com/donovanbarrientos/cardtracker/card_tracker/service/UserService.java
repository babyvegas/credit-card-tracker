package com.donovanbarrientos.cardtracker.card_tracker.service;

import com.donovanbarrientos.cardtracker.card_tracker.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    void deletebyId(Long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
