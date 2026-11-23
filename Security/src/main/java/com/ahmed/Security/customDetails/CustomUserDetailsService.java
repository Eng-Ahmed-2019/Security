package com.ahmed.Security.customDetails;

import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class CustomUserDetailsService implements UserDetailsService{

    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        if (username.equals("ahmed")) {
            return new CustomUserDetails(
                    "ahmed",
                    passwordEncoder.encode("123456"),
                    "USER"
            );
        }

        if (username.equals("admin")) {
            return new CustomUserDetails(
                    "admin",
                    passwordEncoder.encode("admin123"),
                    "ADMIN"
            );
        }

        throw new UsernameNotFoundException("User not found");
    }
}