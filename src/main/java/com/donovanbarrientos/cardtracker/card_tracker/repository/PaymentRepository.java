package com.donovanbarrientos.cardtracker.card_tracker.repository;

import com.donovanbarrientos.cardtracker.card_tracker.model.Card;
import com.donovanbarrientos.cardtracker.card_tracker.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByCard(Card card);

    List<Payment> findByCardId(Long cardId);

    @Query("SELECT p FROM Payment p WHERE p.card.user.id = :userId")
    List<Payment> findByUserId(Long userId);

    List<Payment> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT p FROM Payment p WHERE p.card.id = :cardId AND p.paymentDate BETWEEN :startDate AND :endDate")
    List<Payment> findByCardIdAndPaymentDateBetween(Long cardId, LocalDate startDate, LocalDate endDate);
}
