package com.example.E_commerce.Service;

import com.example.E_commerce.DTO.UserResponse;

import java.util.List;

public interface AdminUserService {

    // LIST / SEARCH
    List<UserResponse> getAllActiveUsers();              // /admin/users?status=ACTIVE
    UserResponse getUserById(Long id);                  // /admin/users/{id}
    UserResponse searchByUsername(String username);     // /admin/users/search?username=
    UserResponse searchByEmail(String email);           // /admin/users/search?email=

    // MANAGE
    void deactivateUser(Long id);
    void activateUser(Long id);
// /admin/users/{id}/status
}
