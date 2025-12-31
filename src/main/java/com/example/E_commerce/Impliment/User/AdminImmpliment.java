package com.example.E_commerce.Impliment.User;

import com.example.E_commerce.DTO.User.UserResponse;
import com.example.E_commerce.Repo.User.UserRepo;
import com.example.E_commerce.Service.User.AdminUserService;
import com.example.E_commerce.modal.User.Role;
import com.example.E_commerce.modal.User.User;
import com.example.E_commerce.modal.User.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminImmpliment implements AdminUserService {

    @Autowired
    private UserRepo userRepo;

    // ================= GET ALL ACTIVE USERS =================
    @Override
    public List<UserResponse> getAllActiveUsers() {

        List<User> users = userRepo.findByStatus(UserStatus.ACTIVE);

        return users.stream()
                .filter(user -> user.getRole() == Role.USER) // 🚫 exclude ADMIN
                .map(this::toResponse)
                .toList();
    }

    // ================= GET USER BY ID =================
    @Override
    public UserResponse getUserById(Long id) {

        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() == Role.ADMIN) {
            throw new RuntimeException("Admin accounts cannot be accessed here");
        }

        return toResponse(user);
    }

    // ================= SEARCH BY USERNAME =================
    @Override
    public UserResponse searchByUsername(String username) {

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() == Role.ADMIN) {
            throw new RuntimeException("Admin accounts cannot be accessed here");
        }

        return toResponse(user);
    }

    // ================= SEARCH BY EMAIL =================
    @Override
    public UserResponse searchByEmail(String email) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() == Role.ADMIN) {
            throw new RuntimeException("Admin accounts cannot be accessed here");
        }

        return toResponse(user);
    }

    // ================= DEACTIVATE USER =================
    @Override
    public void deactivateUser(Long id) {

        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🚫 Protect admins
        if (user.getRole() == Role.ADMIN) {
            throw new RuntimeException("Admin accounts cannot be deactivated");
        }

        user.setStatus(UserStatus.INACTIVE);
        userRepo.save(user);
    }

    // ================= ACTIVATE USER =================
    @Override
    public void activateUser(Long id) {

        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🚫 Protect admins
        if (user.getRole() == Role.ADMIN) {
            throw new RuntimeException("Admin accounts cannot be activated");
        }

        user.setStatus(UserStatus.ACTIVE);
        userRepo.save(user);
    }

    // ================= PRIVATE MAPPER =================
    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getStatus().name(),
                null,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
