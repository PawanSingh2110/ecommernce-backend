package com.example.E_commerce.Controller.User;

import com.example.E_commerce.DTO.User.UserResponse;
import com.example.E_commerce.Service.User.AdminUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
public class AdminController {

    private final AdminUserService adminUserService;

    public AdminController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    // GET /admin/users
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllActiveUsers() {
        return ResponseEntity.ok(adminUserService.getAllActiveUsers());
    }

    // GET /admin/users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(adminUserService.getUserById(id));
    }

    // GET /admin/users/search/by-username?username=john
    @GetMapping("/search/by-username")
    public ResponseEntity<UserResponse> searchByUsername(
            @RequestParam String username
    ) {
        return ResponseEntity.ok(
                adminUserService.searchByUsername(username)
        );
    }

    // GET /admin/users/search/by-email?email=test@mail.com
    @GetMapping("/search/by-email")
    public ResponseEntity<UserResponse> searchByEmail(
            @RequestParam String email
    ) {
        return ResponseEntity.ok(
                adminUserService.searchByEmail(email)
        );
    }

    // PUT /admin/users/{id}/deactivate
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateUser(@PathVariable Long id) {
        adminUserService.deactivateUser(id);
        return ResponseEntity.ok("User deactivated successfully");
    }

    // PUT /admin/users/{id}/activate
    @PutMapping("/{id}/activate")
    public ResponseEntity<?> activateUser(@PathVariable Long id) {
        adminUserService.activateUser(id);
        return ResponseEntity.ok("User activated successfully");
    }
}
