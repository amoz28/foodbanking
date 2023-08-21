package com.bezkoder.springjwt.payload.request;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data

public class SettingsRequest {

    private String username;

    private Long creditConversion;

    private String deliveryDays;

    private Long withdrawals;

    private Long updatedBy;

    private LocalDate updatedAt = LocalDate.now();

}
