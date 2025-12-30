package com.example.E_commerce.Controller.User;

import com.example.E_commerce.DTO.User.UserResponse;
import com.example.E_commerce.Service.User.AdminUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
public class AdminContller {   // you can rename to AdminController

    private final AdminUserService adminUserService;

    public AdminContller(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    // GET /admin/users  -> all ACTIVE users
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllActiveUsers() {
        List<UserResponse> users = adminUserService.getAllActiveUsers();
        return ResponseEntity.ok(users);
    }

    // GET /admin/users/{id} -> user by id
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse user = adminUserService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // GET /admin/users/search/by-username?username=john
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search/by-username")
    public ResponseEntity<UserResponse> searchByUsername(@RequestParam String username) {
        UserResponse user = adminUserService.searchByUsername(username);
        return ResponseEntity.ok(user);
    }

    // GET /admin/users/search/by-email?email=abc@test.com
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search/by-email")
    public ResponseEntity<UserResponse> searchByEmail(@RequestParam String email) {
        UserResponse user = adminUserService.searchByEmail(email);
        return ResponseEntity.ok(user);
    }

    // PUT /admin/users/{id}/deactivate
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateUser(@PathVariable Long id) {
        adminUserService.deactivateUser(id);
        return ResponseEntity.ok("User deactivated successfully");
    }

    // PUT /admin/users/{id}/activate
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/activate")
    public ResponseEntity<?> activateUser(@PathVariable Long id) {
        adminUserService.activateUser(id);
        return ResponseEntity.ok("User activated successfully");
    }

}
