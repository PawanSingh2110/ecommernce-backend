package com.example.E_commerce.Service;

import com.example.E_commerce.DTO.CreateUserRequest;
import com.example.E_commerce.DTO.UserResponse;
import com.example.E_commerce.DTO.LoginRequest;

import java.util.List;

// ❌ REMOVED @Service HERE
// @Service  ← removed

public interface UserService {

    // PUBLIC
    UserResponse createUser(CreateUserRequest request);   // /auth/register
    UserResponse login(LoginRequest request);             // /auth/login

    // SELF (logged-in user)
    UserResponse getCurrentUserByEmail(String email);
}
