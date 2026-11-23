package com.ahmed.Security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SecurityDemoController {

    @GetMapping("/public")
    public String publicEndpoint() {
        return "Anyone can access me!";
    }

    @GetMapping("/private")
    public String privateEndpoint(){
        return "Authenticated users only!";
    }
}