package com.skinclear.skinclearbackend.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FirebaseTokenFilter extends OncePerRequestFilter {
    private boolean validateFirebaseToken(String idToken) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            return decodedToken != null;
        } catch (FirebaseAuthException e) {
            return false;
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        if (token != null && validateFirebaseToken(token)) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(null, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        if (token != null) {

            try {

                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);

                String role = decodedToken.getClaims().get("role").toString();

                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(role));

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(null, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                SecurityContextHolder.clearContext();
            }

        }

        filterChain.doFilter(request, response);
    }
}