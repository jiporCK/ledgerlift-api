package com.example.ledgerlift.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "receipts")
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String receiptId;

    private String transactionId;

    private BigDecimal amount;

    private String message;

    private LocalDateTime date;

    @ManyToOne
    private User user;

    @ManyToOne
    private Event event;

    @ManyToOne
    private Organization organization;

}
