package com.bezkoder.springjwt.controllers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.bezkoder.springjwt.payload.request.ForgotPasswordRequest;
import com.bezkoder.springjwt.payload.request.ResetPasswordRequest;
import com.bezkoder.springjwt.payload.request.UserRequest;
import com.bezkoder.springjwt.service.Impl.PreAuthServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.bezkoder.springjwt.payload.response.JwtResponse;
import com.bezkoder.springjwt.repository.RoleRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import com.bezkoder.springjwt.security.jwt.JwtUtils;
import com.bezkoder.springjwt.security.services.UserDetailsImpl;
import org.springframework.web.context.support.HttpRequestHandlerServlet;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class PreAuthenticationController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

    @Autowired
    private PreAuthServiceImpl preAuthServiceImpl;

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequest signUpRequest) {
		return preAuthServiceImpl.signUp(signUpRequest);
	}

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody UserRequest loginRequest, HttpServletRequest httpServletRequest) throws UnknownHostException {
		log.info("REQUEST 2 "+ httpServletRequest.getRequestURI());
		InetAddress inetAddress = InetAddress.getLocalHost();
		log.info("REQUEST 5 "+ inetAddress.getHostAddress());
		log.info("REQUEST 6 "+ inetAddress.getAddress().toString());
		log.info("REQUEST 7 "+ inetAddress.getCanonicalHostName());
		log.info("REQUEST 8 "+ inetAddress.getHostAddress());
		log.info("REQUEST 9 "+ inetAddress.getHostName());

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles)
        );
	}

	@PostMapping("/enableAccount")
	public ResponseEntity<?> enableAccount(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
		return preAuthServiceImpl.enableAccount(resetPasswordRequest.getEmail(), resetPasswordRequest.getToken());
	};

	@PostMapping("/forgotPassword")
	public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
		return preAuthServiceImpl.validateUsernameOrEmail(forgotPasswordRequest.getUsername());
	};

	@PostMapping("/resetPassword")
	public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
		return preAuthServiceImpl.validateEmailAndToken(resetPasswordRequest.getEmail(), resetPasswordRequest.getPassword(), resetPasswordRequest.getToken());
	};

	@GetMapping("/resendToken")
	public ResponseEntity<?> resendToken(@Valid @RequestParam("email") String email) {
		return preAuthServiceImpl.resendToken(email);
	};

	@GetMapping("/testMail")
    public void sendMail(){
	    preAuthServiceImpl.emailNotification("amosokomayin@gmail.com","Food banking is testing!!!", "Testing the app");
    }

}
