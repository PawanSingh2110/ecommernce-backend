package com.example.E_commerce.Repo;

import com.example.E_commerce.modal.User;
import com.example.E_commerce.modal.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepo extends JpaRepository<User ,Long> {
    // ✅ Find by email (login)
    Optional<User> findByEmail(String email);

    // ✅ Check if exists (register validation)
    boolean existsByEmail(String email);
    boolean existsByusername(String username);

    // ✅ Find active users only
    List<User> findByStatus(UserStatus status);
    Optional<User> findByUsername(String username);
}
