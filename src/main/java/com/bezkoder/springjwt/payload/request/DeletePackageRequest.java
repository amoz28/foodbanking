package com.bezkoder.springjwt.payload.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DeletePackageRequest {

    private Long packageId;

    @NotNull
    private String username;

}
