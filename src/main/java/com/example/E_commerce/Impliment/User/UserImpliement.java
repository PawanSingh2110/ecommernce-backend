package com.example.E_commerce.Impliment.User;

import com.example.E_commerce.DTO.User.CreateUserRequest;
import com.example.E_commerce.DTO.User.LoginRequest;
import com.example.E_commerce.DTO.User.UserResponse;
import com.example.E_commerce.Repo.User.UserRepo;
import com.example.E_commerce.Service.User.JwtService;
import com.example.E_commerce.Service.User.UserService;
import Role;
import com.example.E_commerce.modal.User.User;
import com.example.E_commerce.modal.User.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;   // ✅ ADDED THIS IMPORT

import java.time.LocalDateTime;

@Service   // ✅ ADDED: this is now the actual service bean
public class UserImpliement implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    // create user
    @Override
    public UserResponse createUser(CreateUserRequest request) {
        // 1. VALIDATION CHECKS
        String email = request.getEmail();
        String username = request.getUsername();

        if (userRepo.existsByusername(username)) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepo.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        if (request.getPassword().length() < 6) {
            throw new RuntimeException("Password should be greater than 6 letters");
        }

        // 2. ROLE HANDLING (JSON role or default USER)
        String roleStr = request.getRole();   // from JSON (can be null)
        Role role = Role.USER;                // default

        if (roleStr != null && !roleStr.isBlank()) {
            try {
                role = Role.valueOf(roleStr.toUpperCase());  // "ADMIN" -> Role.ADMIN
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid role: " + roleStr);
            }
        }

        // 3. CREATE USER
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);                      // <- from logic above
        user.setStatus(UserStatus.ACTIVE);       // default status
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // 4. SAVE TO DB
        User savedUser = userRepo.save(user);

        // 5. BUILD RESPONSE
        return new UserResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getStatus().name(),
                null,                      // no token on register
                savedUser.getCreatedAt(),
                savedUser.getUpdatedAt()
        );
    }


    @Override
    public UserResponse login(LoginRequest request) {
        String email = request.getEmail();
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new RuntimeException("Account not active");
        }

        String jwtToken = jwtService.generateToken(email);

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getStatus().name(),
                jwtToken,                            // token on login
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    @Override
    public UserResponse getCurrentUserByEmail(String email) {

        // 1. Find user in DB
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getStatus().name(),
                null,                            // token on login
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }



}
