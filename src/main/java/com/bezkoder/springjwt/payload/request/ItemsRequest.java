package com.bezkoder.springjwt.payload.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
public class ItemsRequest {

    private Long id;

    private Long packageId;

    @NotNull
    private String username;

    private String name;

    private String item_image;

    private Integer credit_price;

}
