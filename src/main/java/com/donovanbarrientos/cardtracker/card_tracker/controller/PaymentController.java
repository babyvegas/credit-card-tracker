package com.donovanbarrientos.cardtracker.card_tracker.controller;

import com.donovanbarrientos.cardtracker.card_tracker.model.Card;
import com.donovanbarrientos.cardtracker.card_tracker.model.Payment;
import com.donovanbarrientos.cardtracker.card_tracker.model.User;
import com.donovanbarrientos.cardtracker.card_tracker.service.CardService;
import com.donovanbarrientos.cardtracker.card_tracker.service.PaymentService;
import com.donovanbarrientos.cardtracker.card_tracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final CardService cardService;
    private final UserService userService;

    @Autowired
    public PaymentController(PaymentService paymentService, CardService cardService, UserService userService) {
        this.paymentService = paymentService;
        this.cardService = cardService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<Payment> payments = paymentService.findByUserId(user.getId());
            return ResponseEntity.ok(payments);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> userOpt = userService.findByUsername(username);
        Optional<Payment> paymentOpt = paymentService.findById(id);

        if (userOpt.isPresent() && paymentOpt.isPresent()) {
            User user = userOpt.get();
            Payment payment = paymentOpt.get();

            // Verify that the payment belongs to a card owned by the authenticated user
            if (payment.getCard().getUser().getId().equals(user.getId())) {
                return ResponseEntity.ok(payment);
            }
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/card/{cardId}")
    public ResponseEntity<List<Payment>> getPaymentsByCardId(@PathVariable Long cardId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> userOpt = userService.findByUsername(username);
        Optional<Card> cardOpt = cardService.findById(cardId);

        if (userOpt.isPresent() && cardOpt.isPresent()) {
            User user = userOpt.get();
            Card card = cardOpt.get();

            // Verify that the card belongs to the authenticated user
            if (card.getUser().getId().equals(user.getId())) {
                List<Payment> payments = paymentService.findByCardId(cardId);
                return ResponseEntity.ok(payments);
            }
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> userOpt = userService.findByUsername(username);
        Optional<Card> cardOpt = cardService.findById(payment.getCard().getId());

        if (userOpt.isPresent() && cardOpt.isPresent()) {
            User user = userOpt.get();
            Card card = cardOpt.get();

            // Verify that the card belongs to the authenticated user
            if (card.getUser().getId().equals(user.getId())) {
                payment.setCard(card);
                Payment savedPayment = paymentService.save(payment);
                return ResponseEntity.status(HttpStatus.CREATED).body(savedPayment);
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable Long id, @RequestBody Payment paymentDetails) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> userOpt = userService.findByUsername(username);
        Optional<Payment> paymentOpt = paymentService.findById(id);

        if (userOpt.isPresent() && paymentOpt.isPresent()) {
            User user = userOpt.get();
            Payment payment = paymentOpt.get();

            // Verify that the payment belongs to a card owned by the authenticated user
            if (payment.getCard().getUser().getId().equals(user.getId())) {
                payment.setAmount(paymentDetails.getAmount());
                payment.setPaymentDate(paymentDetails.getPaymentDate());
                payment.setStatus(paymentDetails.getStatus());

                Payment updatedPayment = paymentService.save(payment);
                return ResponseEntity.ok(updatedPayment);
            }
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> userOpt = userService.findByUsername(username);
        Optional<Payment> paymentOpt = paymentService.findById(id);

        if (userOpt.isPresent() && paymentOpt.isPresent()) {
            User user = userOpt.get();
            Payment payment = paymentOpt.get();

            // Verify that the payment belongs to a card owned by the authenticated user
            if (payment.getCard().getUser().getId().equals(user.getId())) {
                paymentService.deleteById(id);
                return ResponseEntity.noContent().build();
            }
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Payment>> getPaymentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<Payment> payments = paymentService.findByDateRange(start, end);

            // Filter to only include payments for the authenticated user's cards
            payments = payments.stream()
                    .filter(payment -> payment.getCard().getUser().getId().equals(user.getId()))
                    .toList();

            return ResponseEntity.ok(payments);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
