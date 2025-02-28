package com.donovanbarrientos.cardtracker.card_tracker.service.impl;

import com.donovanbarrientos.cardtracker.card_tracker.model.Payment;
import com.donovanbarrientos.cardtracker.card_tracker.repository.PaymentRepository;
import com.donovanbarrientos.cardtracker.card_tracker.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return paymentRepository.findById(id);
    }

    @Override
    public List<Payment> findByCardId(Long cardId) {
        return paymentRepository.findByCardId(cardId);
    }

    @Override
    public List<Payment> findByUserId(Long userId) {
        return paymentRepository.findByUserId(userId);
    }

    @Override
    public List<Payment> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return paymentRepository.findByPaymentDateBetween(startDate, endDate);
    }

    @Override
    public List<Payment> findByCardIdAndDateRange(Long cardId, LocalDate startDate, LocalDate endDate) {
        return paymentRepository.findByCardIdAndPaymentDateBetween(cardId, startDate, endDate);
    }

    @Override
    public void deleteById(Long id) {
        paymentRepository.deleteById(id);
    }
}
