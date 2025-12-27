package com.example.E_commerce.Controller;

import com.example.E_commerce.DTO.CreateUserRequest;
import com.example.E_commerce.DTO.LoginRequest;
import com.example.E_commerce.DTO.UserResponse;
import com.example.E_commerce.DTO.*;
import com.example.E_commerce.Service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    // ✅ REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request,
                                        HttpServletResponse response) {
        try {
            UserResponse userResponse = userService.createUser(request);

            // ✅ SUCCESS MESSAGE + COOKIE (optional)
            Cookie cookie = new Cookie("userId", userResponse.getId().toString());
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(24 * 60 * 60);  // 1 day
            response.addCookie(cookie);

            return ResponseEntity.ok(
                    Map.of("success", true, "message", "User created successfully!", "data", userResponse)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // ✅ LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request,
                                       HttpServletResponse response) {
        try {
            UserResponse userResponse = userService.login(request);

            // ✅ JWT COOKIE (Secure!)
            Cookie tokenCookie = new Cookie("jwtToken", userResponse.getAccessToken());
            tokenCookie.setHttpOnly(true);    // JS can't read!
            tokenCookie.setPath("/");
            tokenCookie.setMaxAge(7 * 24 * 60 * 60);  // 7 days
            response.addCookie(tokenCookie);

            // ✅ USER ID COOKIE
            Cookie userCookie = new Cookie("userId", userResponse.getId().toString());
            userCookie.setHttpOnly(true);
            userCookie.setPath("/");
            userCookie.setMaxAge(7 * 24 * 60 * 60);
            response.addCookie(userCookie);

            return ResponseEntity.ok(
                    Map.of("success", true, "message", "Login successful!", "data", userResponse)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(401)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}
