package com.ahmed.Security.configuration;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.web.cors.CorsConfiguration;
import com.ahmed.Security.filter.JwtAuthenticationFilter;
import org.springframework.security.core.userdetails.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
        HttpSecurity security,
        JwtAuthenticationFilter filter
    ) throws Exception{

        // security filter chain
        security
        .cors(Customizer.withDefaults())
        .csrf(c->c.disable())
        .sessionManagement(s->s
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            // يعني السيرفر مش بيعتمد على Session لتخزين حالة تسجيل الدخول
        )
        .authorizeHttpRequests(auth-> auth
            .requestMatchers("/api/public").permitAll()
            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers("/api/password/**").permitAll()
            .requestMatchers("/api/private").authenticated()
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            .requestMatchers("/api/user/**").hasAnyRole("USER","ADMIN")
            .anyRequest().authenticated()
        )
        .httpBasic(Customizer.withDefaults())
        .formLogin(f->f.disable()) // إلغاء صفحة Login الافتراضية
        .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return security.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // @Bean
    // public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
    //     UserDetails user = User.builder()
    //         .username("ahmed")
    //         .password(passwordEncoder.encode("123456"))
    //         .roles("USER")
    //         .build();

    //     return new InMemoryUserDetailsManager(user);
    // }

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration configuration
    )throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of(
            "GET","POST","PUT","DELETE","PATCH","OPTIONS"
        ));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(false); //هل نسمح بإرسال credentials زي Cookies أم لا.

        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}