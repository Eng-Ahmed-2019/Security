package com.ahmed.Security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/api/password")
public class PasswordDemoController {

    private final PasswordEncoder passwordEncoder;

    public PasswordDemoController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/encode")
    public String encode(){
        return passwordEncoder.encode("123456");
    }

    @GetMapping("/matches")
    public boolean matches(){
        String oldPassword = "123456";
        String newPassword = "1234567";

        String encodedPassword = passwordEncoder.encode(newPassword);

        return passwordEncoder.matches(oldPassword, encodedPassword);
    }
}