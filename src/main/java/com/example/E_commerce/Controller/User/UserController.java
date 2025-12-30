package com.example.E_commerce.Controller.User;

import com.example.E_commerce.DTO.User.CreateUserRequest;
import com.example.E_commerce.DTO.User.LoginRequest;
import com.example.E_commerce.DTO.User.UserResponse;
import com.example.E_commerce.Service.User.UserService;
import com.example.E_commerce.modal.User.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ================= AUTH =================

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody CreateUserRequest request) {
        UserResponse user = userService.createUser(request);
        return ResponseEntity.ok(
                Map.of("success", true, "data", user)
        );
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        UserResponse user = userService.login(request);

        Cookie jwt = new Cookie("jwtToken", user.getAccessToken());
        jwt.setHttpOnly(true);
        jwt.setPath("/");
        jwt.setMaxAge(7 * 24 * 60 * 60);
        jwt.setSecure(false); // set true in production (HTTPS)

        response.addCookie(jwt);

        return ResponseEntity.ok(
                Map.of("success", true, "data", user)
        );
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok(
                Map.of("success", true)
        );
    }

    // ================= USER =================

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/users/me")
    public ResponseEntity<?> me(Authentication authentication) {

        User userEntity = (User) authentication.getPrincipal();

        UserResponse user =
                userService.getCurrentUserByEmail(userEntity.getEmail());

        return ResponseEntity.ok(
                Map.of("success", true, "data", user)
        );
    }
}
