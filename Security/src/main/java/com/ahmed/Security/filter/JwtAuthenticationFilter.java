package com.ahmed.Security.filter;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.ServletException;
import com.ahmed.Security.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter { // الـ Filter بيتنفذ مرة لكل Request

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService) {

        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException,IOException{

        String authHeader = request.getHeader("Authorization");

        if (authHeader==null||!authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        String username;

        try{
            username = jwtService.extractUsername(token);
        }catch(JwtException | IllegalArgumentException ex){
            filterChain.doFilter(request, response);
            return;
        }

        if (username!=null&&
            // 	يحفظ المستخدم الحالي للطلب
            SecurityContextHolder.getContext().getAuthentication()==null
        ) {
            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(username);

            try {
                if (jwtService.isTokenValid(token, userDetails)) {

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    // ده بيخلّي Spring Security يعتبر المستخدم Authenticated، وبالتالي يقدر يفحص الـ Roles والصلاحيات.
                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                }
            } catch (JwtException | IllegalArgumentException ex){}

            // 	يكمل مرور الـ Request لباقي الـ Filters
            filterChain.doFilter(request, response);
        }
    }
}