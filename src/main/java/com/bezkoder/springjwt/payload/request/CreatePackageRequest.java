package com.bezkoder.springjwt.payload.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreatePackageRequest {

    private Long packageId;

    @NotNull
    private String username;

    @NotNull
    private String package_name;

    @NotNull
    private Long package_price;

    @NotNull
    private String package_image;

    @NotNull
    private Integer tenor;

    @JsonIgnore
    private String created_by;

    private Integer updated_by;

}
