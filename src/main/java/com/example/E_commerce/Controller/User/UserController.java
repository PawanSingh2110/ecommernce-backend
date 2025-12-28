package com.example.E_commerce.Controller.User;

import com.example.E_commerce.DTO.User.CreateUserRequest;
import com.example.E_commerce.DTO.User.LoginRequest;
import com.example.E_commerce.DTO.User.UserResponse;
import com.example.E_commerce.Service.User.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody CreateUserRequest request
    ) {
        UserResponse user = userService.createUser(request);
        return ResponseEntity.ok(
                Map.of("success", true, "data", user)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        UserResponse user = userService.login(request);

        Cookie jwt = new Cookie("jwtToken", user.getAccessToken());
        jwt.setHttpOnly(true);
        jwt.setPath("/");
        jwt.setMaxAge(7 * 24 * 60 * 60);
        jwt.setSecure(false);

        response.addCookie(jwt);

        return ResponseEntity.ok(
                Map.of("success", true, "data", user)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of("success", true));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me() {
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(Map.of("success", false));
        }

        String email = auth.getName();
        UserResponse user =
                userService.getCurrentUserByEmail(email);

        return ResponseEntity.ok(
                Map.of("success", true, "data", user)
        );
    }
}
