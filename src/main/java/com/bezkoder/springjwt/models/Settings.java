package com.bezkoder.springjwt.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "setting")

public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long creditConversion;

    private String deliveryDays;

    private Long withdrawals;

    private String paymentPublicKey;

    private String paymentSecretKey;

    private String updatedBy;

    private LocalDate updatedAt = LocalDate.now();

}
