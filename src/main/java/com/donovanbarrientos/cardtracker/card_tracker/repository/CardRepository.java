package com.donovanbarrientos.cardtracker.card_tracker.repository;

import com.donovanbarrientos.cardtracker.card_tracker.model.Card;
import com.donovanbarrientos.cardtracker.card_tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByUser(User user);

    @Query("SELECT c FROM Card c WHERE c.user.id = :userId")
    List<Card> findByUserId(Long userId);

    @Query("SELECT c FROM Card c WHERE c.user.id = :userId AND c.paymentDueDay = :day")
    List<Card> findByUserIdAndPaymentDueDay(Long userId, Integer day);

    @Query("SELECT c FROM Card c WHERE c.user.id = :userId AND c.paymentDueDay BETWEEN :startDay AND :endDay")
    List<Card> findByUserIdAndPaymentDueDayBetween(Long userId, Integer startDay, Integer endDay);
}
