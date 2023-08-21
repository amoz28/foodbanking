package com.bezkoder.springjwt.service;


import com.bezkoder.springjwt.payload.request.UserRequest;
import org.springframework.http.ResponseEntity;

public interface PreAuthService {

    void sendMail (String toEmail, String body);

    ResponseEntity validateUsernameOrEmail(String username);

    ResponseEntity validateEmailAndToken(String email, String password, String token);

    ResponseEntity enableAccount(String email, String token);

    ResponseEntity resendToken(String email);

    ResponseEntity signUp(UserRequest signUpRequest);

    void emailNotification(String email, String body, String subject);

}
