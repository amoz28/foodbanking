package com.bezkoder.springjwt.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "deposit")

public class Deposit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long user_id;

    private Integer type;

    private String description;

    @NotNull(message = "please provide the transactionId from payment gateway")
    private String tranId;

    private Double amount_paid;

    private Double credits_received;
}
