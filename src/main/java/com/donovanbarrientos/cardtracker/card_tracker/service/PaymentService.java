package com.donovanbarrientos.cardtracker.card_tracker.service;

import com.donovanbarrientos.cardtracker.card_tracker.model.Payment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaymentService {
    Payment save(Payment payment);
    Optional<Payment> findById(Long id);
    List<Payment> findByCardId(Long cardId);
    List<Payment> findByUserId(Long userId);
    List<Payment> findByDateRange(LocalDate startDate, LocalDate endDate);
    List<Payment> findByCardIdAndDateRange(Long cardId, LocalDate startDate, LocalDate endDate);
    void deleteById(Long id);
}
