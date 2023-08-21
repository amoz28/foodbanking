package com.bezkoder.springjwt.payload.request;

import lombok.Data;

import java.util.Set;

import javax.validation.constraints.*;

@Data
public class UserRequest {
    @Size(min = 3, max = 20)
    private String username;
 
    @Size(max = 50)
    @Email
    private String email;

    @Size(min=10, max = 15)
    private String phone;

    private Set<String> role;
    
    @Size(min = 6, max = 40)
    private String password;

}
