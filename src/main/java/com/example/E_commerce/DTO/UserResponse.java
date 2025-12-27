package com.example.E_commerce.DTO;  // ✅ Good package name

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String status;
    private String accessToken;  // ✅ Fixed spelling (camelCase)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
