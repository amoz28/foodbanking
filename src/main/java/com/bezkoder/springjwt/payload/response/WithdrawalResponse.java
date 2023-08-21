package com.bezkoder.springjwt.payload.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class WithdrawalResponse {

    private int status;

    private String message;

    private List<String> errorList;

    private List<String> successList;


    public WithdrawalResponse(int status, String message, List<String> errorList, List<String> successList) {
        this.status = status;
        this.message = message;
        this.errorList = errorList;
        this.successList = successList;
    }

}
