package com.bezkoder.springjwt.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "withdraw")

public class Withdrawal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long created_at;

    private Long created_by;

    private String status;

    private String responseMsg;

    private Long updated_at;

    private Long updated_by;
}
