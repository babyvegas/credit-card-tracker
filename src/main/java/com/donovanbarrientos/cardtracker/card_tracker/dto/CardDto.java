package com.donovanbarrientos.cardtracker.card_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {
    private Long id;
    private Long userId;
    private String cardName;
    private String cardType;
    private Integer paymentDueDay;
    private BigDecimal creditLimit;
    private BigDecimal currentBalance;
}
