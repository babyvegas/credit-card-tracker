package com.donovanbarrientos.cardtracker.card_tracker.service;

import com.donovanbarrientos.cardtracker.card_tracker.model.Card;

import java.util.List;
import java.util.Optional;

public interface CardService {
    Card save(Card card);
    Optional<Card> findById(Long id);
    List<Card> findByUserId(Long userId);
    List<Card> findAll();
    void deleteById(Long id);
    List<Card> findUpcomingPayments(Long userId, int daysAhead);
}
