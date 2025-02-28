package com.donovanbarrientos.cardtracker.card_tracker.service.impl;

import com.donovanbarrientos.cardtracker.card_tracker.model.Card;
import com.donovanbarrientos.cardtracker.card_tracker.repository.CardRepository;
import com.donovanbarrientos.cardtracker.card_tracker.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public Card save(Card card) {
        return cardRepository.save(card);
    }

    @Override
    public Optional<Card> findById(Long id) {
        return cardRepository.findById(id);
    }

    @Override
    public List<Card> findByUserId(Long userId) {
        return cardRepository.findByUserId(userId);
    }

    @Override
    public List<Card> findAll() {
        return cardRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        cardRepository.deleteById(id);
    }

    @Override
    public List<Card> findUpcomingPayments(Long userId, int daysAhead) {
        LocalDate today = LocalDate.now();
        int currentDay = today.getDayOfMonth();
        int endDay = today.plusDays(daysAhead).getDayOfMonth();

        if (endDay < currentDay) {
            List<Card> thisMonthCards = cardRepository.findByUserIdAndPaymentDueDayBetween(
                    userId, currentDay, 31
            );

            List<Card> nextMonthCards = cardRepository.findByUserIdAndPaymentDueDayBetween(
                    userId, 1, endDay
            );

            thisMonthCards.addAll(nextMonthCards);
            return thisMonthCards;
        } else {
            return cardRepository.findByUserIdAndPaymentDueDayBetween(userId, currentDay, endDay);
        }
    }
}
