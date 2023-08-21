package com.bezkoder.springjwt.payload.response;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDetailsResponse {

    private String status;

    private String firstname;

    private String lastname;

    private String sex;

    private Integer age;

    private String marital_status;

    private int family_sum;

    private String username;

    private String email;

    private String phone;

    private String profilePicSource;

    private String address;

    private String city;

    private String state;

    private String updatedAt;

    private String createdAt;

    private String creditBalance;

    private LocalDate nextDeliveryDate;

    private String token;

    private boolean isEnabled;

}
