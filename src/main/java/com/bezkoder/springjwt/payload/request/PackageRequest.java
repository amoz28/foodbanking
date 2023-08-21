package com.bezkoder.springjwt.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PackageRequest {
    @NotBlank
    private String username;

    private Integer unit;

    @NotBlank(message = "nextDeliveryDate is required")
    private String nextDeliveryDate;

    private Integer packageId;


}
