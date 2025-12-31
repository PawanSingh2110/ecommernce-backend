package com.example.E_commerce.Config;

import com.example.E_commerce.Repo.User.UserRepo;
import com.example.E_commerce.Service.User.JwtService;
import com.example.E_commerce.modal.User.User;
import com.example.E_commerce.modal.User.UserStatus;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepo userRepo;

    public JwtAuthFilter(JwtService jwtService, UserRepo userRepo) {
        this.jwtService = jwtService;
        this.userRepo = userRepo;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String jwt = null;

        // 1️⃣ Read JWT from cookie
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwtToken".equals(cookie.getName())) {
                    jwt = cookie.getValue();
                    break;
                }
            }
        }

        // No token → unauthenticated, continue
        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String email;
        try {
            email = jwtService.extractUsername(jwt);
        } catch (Exception e) {
            filterChain.doFilter(request, response);
            return;
        }

        // Authenticate only if context is empty
        if (email != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            User user = userRepo.findByEmail(email).orElse(null);

            // User not found or inactive
            if (user == null || user.getStatus() != UserStatus.ACTIVE) {
                filterChain.doFilter(request, response);
                return;
            }

            // Validate token
            if (jwtService.isTokenValid(jwt, user)) {

                // ✅ Authority creation stays here (since User is NOT UserDetails)
                SimpleGrantedAuthority authority =
                        new SimpleGrantedAuthority(
                                "ROLE_" + user.getRole().name()
                        );

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                List.of(authority)
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                // ✅ Authentication ONLY (no role logic beyond this)
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
