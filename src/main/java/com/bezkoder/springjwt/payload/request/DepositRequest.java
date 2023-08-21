package com.bezkoder.springjwt.payload.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepositRequest {

    private String username;

    private Integer type;

    private String description;

    private Double amount_paid;

    private String transactionId;

    private Double credits_received;
}