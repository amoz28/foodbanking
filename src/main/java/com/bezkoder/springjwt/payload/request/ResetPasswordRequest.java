package com.bezkoder.springjwt.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ResetPasswordRequest {
    @NotBlank
    private String email;

    private String password;

    private String token;
}
