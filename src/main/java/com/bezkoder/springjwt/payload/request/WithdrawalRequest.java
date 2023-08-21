package com.bezkoder.springjwt.payload.request;

import com.bezkoder.springjwt.models.Items;
import com.bezkoder.springjwt.models.Withdrawal;
import lombok.Data;

import java.util.List;

@Data
public class WithdrawalRequest {

    private String username;

    private Integer packageId;

    private String deliveryWeek;

    private String withdrawalType;

    private List<Items> items;

}