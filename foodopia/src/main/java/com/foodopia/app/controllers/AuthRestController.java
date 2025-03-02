package com.foodopia.app.controllers;

import com.foodopia.app.dto.LoginRequest;
import com.foodopia.app.dto.RegistrationDto;
import com.foodopia.app.exceptions.UsernameAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.foodopia.app.service.CustomerService;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import jakarta.validation.Valid;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/auth")
public class AuthRestController {

    private final CustomerService customerService;
    private final AuthenticationManager authenticationManager;

    // Secret key for signing the token (in a real application, load this from a secure config)
    @Value("${jwt.secret}")
    private final String jwtSecret = "mySuperSecretKey";
    @Value("${jwt.expiration}")
    private final long jwtExpirationInMs = 86400000;

    @Autowired
    public AuthRestController(CustomerService customerService, AuthenticationManager authenticationManager) {
        this.customerService = customerService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = generateToken(authentication);

        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationDto registrationDto, BindingResult result) {
        // Check for errors in the registration DTO (e.g., password mismatch is handled at the DTO or service level)
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        try {
            customerService.registerNewCustomer(registrationDto);
        } catch (UsernameAlreadyExistsException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }

        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/check-username/{username}")
    public ResponseEntity<Map<String, Object>> checkUsernameAvailability(@PathVariable String username) {
        Map<String, Object> response = new HashMap<>();
        boolean exists = customerService.findByUsername(username).isPresent();

        response.put("available", !exists);
        return ResponseEntity.ok(response);
    }

    private String generateToken(Authentication authentication) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);

        return JWT.create()
                .withSubject(authentication.getName())
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .withClaim("role", authentication.getAuthorities().iterator().next().getAuthority())
                .sign(algorithm);
    }
}