package com.example.E_commerce.Service.User;

import com.example.E_commerce.DTO.User.CreateUserRequest;
import com.example.E_commerce.DTO.User.UserResponse;
import com.example.E_commerce.DTO.User.LoginRequest;

// ❌ REMOVED @Service HERE
// @Service  ← removed

public interface UserService {

    // PUBLIC
    UserResponse createUser(CreateUserRequest request);   // /auth/register
    UserResponse login(LoginRequest request);             // /auth/login

    // SELF (logged-in user)
    UserResponse getCurrentUserByEmail(String email);
}
