package com.example.E_commerce.Impliment;

import com.example.E_commerce.DTO.UserResponse;
import com.example.E_commerce.Repo.UserRepo;
import com.example.E_commerce.Service.AdminUserService;
import com.example.E_commerce.modal.User;
import com.example.E_commerce.modal.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AdminImmpliment implements AdminUserService {

    @Autowired
    private UserRepo userRepo;


    @Override
    public List<UserResponse> getAllActiveUsers() {

        // assume status is an enum Status.ACTIVE
        List<User> users = userRepo.findByStatus(UserStatus.ACTIVE);

        return users.stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRole(),
                        user.getStatus().name(),
                        null,
                        user.getCreatedAt(),
                        user.getUpdatedAt()
                ))
                .toList();
    }


    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getStatus().name(),
                null,                 // no token here
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }


    @Override
    public UserResponse searchByUsername(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getStatus().name(),
                null,                 // no token here
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    @Override
    public UserResponse searchByEmail(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getStatus().name(),
                null,                 // no token here
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    @Override
    public void deactivateUser(Long id) {

        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setStatus(UserStatus.INACTIVE); // or "INACTIVE"
        userRepo.save(user);
    }

    @Override
    public void activateUser(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setStatus(UserStatus.ACTIVE);    // or "ACTIVE"
        userRepo.save(user);              // update in DB [web:91][web:93]
    }


}
