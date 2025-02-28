package com.donovanbarrientos.cardtracker.card_tracker.controller;

import com.donovanbarrientos.cardtracker.card_tracker.dto.CardDto;
import com.donovanbarrientos.cardtracker.card_tracker.model.Card;
import com.donovanbarrientos.cardtracker.card_tracker.model.User;
import com.donovanbarrientos.cardtracker.card_tracker.service.CardService;
import com.donovanbarrientos.cardtracker.card_tracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;
    private final UserService userService;

    @Autowired
    public CardController(CardService cardService, UserService userService) {
        this.cardService = cardService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<CardDto>> getAllCards() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<Card> cards = cardService.findByUserId(user.getId());
            List<CardDto> cardDtos = cards.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(cardDtos);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardDto> getCardById(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> userOpt = userService.findByUsername(username);
        Optional<Card> cardOpt = cardService.findById(id);

        if (userOpt.isPresent() && cardOpt.isPresent()) {
            Card card = cardOpt.get();
            // Verify that the card belongs to the authenticated user
            if (card.getUser().getId().equals(userOpt.get().getId())) {
                return ResponseEntity.ok(convertToDto(card));
            }
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<CardDto> createCard(@RequestBody CardDto cardDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            Card card = new Card();
            card.setUser(user);
            card.setCardName(cardDto.getCardName());
            card.setCardType(cardDto.getCardType());
            card.setPaymentDueDay(cardDto.getPaymentDueDay());
            card.setCreditLimit(cardDto.getCreditLimit());
            card.setCurrentBalance(cardDto.getCurrentBalance());

            Card savedCard = cardService.save(card);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(savedCard));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardDto> updateCard(@PathVariable Long id, @RequestBody CardDto cardDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> userOpt = userService.findByUsername(username);
        Optional<Card> cardOpt = cardService.findById(id);

        if (userOpt.isPresent() && cardOpt.isPresent()) {
            User user = userOpt.get();
            Card card = cardOpt.get();

            // Verify that the card belongs to the authenticated user
            if (card.getUser().getId().equals(user.getId())) {
                card.setCardName(cardDto.getCardName());
                card.setCardType(cardDto.getCardType());
                card.setPaymentDueDay(cardDto.getPaymentDueDay());
                card.setCreditLimit(cardDto.getCreditLimit());
                card.setCurrentBalance(cardDto.getCurrentBalance());

                Card updatedCard = cardService.save(card);
                return ResponseEntity.ok(convertToDto(updatedCard));
            }
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> userOpt = userService.findByUsername(username);
        Optional<Card> cardOpt = cardService.findById(id);

        if (userOpt.isPresent() && cardOpt.isPresent()) {
            User user = userOpt.get();
            Card card = cardOpt.get();

            // Verify that the card belongs to the authenticated user
            if (card.getUser().getId().equals(user.getId())) {
                cardService.deleteById(id);
                return ResponseEntity.noContent().build();
            }
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<CardDto>> getUpcomingPayments(@RequestParam(defaultValue = "7") int days) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<Card> cards = cardService.findUpcomingPayments(user.getId(), days);
            List<CardDto> cardDtos = cards.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(cardDtos);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // Helper method to convert Card to CardDto
    private CardDto convertToDto(Card card) {
        CardDto dto = new CardDto();
        dto.setId(card.getId());
        dto.setUserId(card.getUser().getId());
        dto.setCardName(card.getCardName());
        dto.setCardType(card.getCardType());
        dto.setPaymentDueDay(card.getPaymentDueDay());
        dto.setCreditLimit(card.getCreditLimit());
        dto.setCurrentBalance(card.getCurrentBalance());
        return dto;
    }
}
