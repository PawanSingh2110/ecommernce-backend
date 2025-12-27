package com.example.E_commerce.Service;

import com.example.E_commerce.DTO.CreateUserRequest;
import com.example.E_commerce.DTO.UserResponse;
import com.example.E_commerce.DTO.LoginRequest;


import java.util.List;

public interface UserService {

    // Basic
    UserResponse createUser(CreateUserRequest request);
    UserResponse login(LoginRequest request);

    // Admin
    List<UserResponse> getAllActiveUsers();

    UserResponse getUserById(Long id);

    void deactivateUser(Long id);

    // Search
    UserResponse searchByusername(String username);

    UserResponse searchByEmail(String email);
}
