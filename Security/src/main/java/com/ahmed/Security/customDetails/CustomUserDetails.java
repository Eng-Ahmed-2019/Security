package com.ahmed.Security.customDetails;

import java.util.List;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class CustomUserDetails implements UserDetails{

    private final String role;
    private final String userName;
    private final String password;

    public CustomUserDetails(String username, String password,String role) {
        this.userName = username;
        this.password = password;
        this.role = role;
    }

    // بيرجع الصلاحيات.
    @Override
    public Collection<? extends GrantedAuthority>getAuthorities(){
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

                    //باقي الـ Methods بنرجع true مؤقتاً عشان الحساب يكون صالح للاستخدام.

                    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}