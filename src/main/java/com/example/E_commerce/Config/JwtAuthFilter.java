package com.example.E_commerce.Config;

import com.example.E_commerce.Repo.UserRepo;
import com.example.E_commerce.Service.JwtService;
import com.example.E_commerce.modal.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private  final JwtService jwtService;
    private final UserRepo userRepo;

    public JwtAuthFilter(JwtService jwtService, UserRepo userRepo) {
        this.jwtService = jwtService;
        this.userRepo = userRepo;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        1. Get Authorization header = done
//        2. If header missing → let request continue = done
//        3. If header exists:
//        - Extract token
//                - Validate token
//                - Extract identity
//                - Create authentication object
//        - Store it in security context
//        4. Continue request
        // step one if no header call for header
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        //Ignore unauthenticated requests if header  empty and not start with Bearer
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response); // not there stop the process
            return;
        }

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token=authHeader.substring(7);
            try{
                email=jwtService.extractEmail(token);
            } catch (Exception e) {
                filterChain.doFilter(request, response);
                return;
            }

        }




        var optionalUser = userRepo.findByEmail(email);
        if (email != null && optionalUser.isPresent()) {
            User user = optionalUser.get();

            String roleName = "ROLE_" + user.getRole().name();

            SimpleGrantedAuthority authority =
                    new SimpleGrantedAuthority(roleName);
            UsernamePasswordAuthenticationToken auth =  new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    List.of(authority)
            );
            SecurityContextHolder.getContext().setAuthentication(auth);

        }

        filterChain.doFilter(request,response);


    }
}
