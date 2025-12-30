package com.example.E_commerce.DTO.Category;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CategoryRequestDTO {

    private String name;
    private String description;
    private MultipartFile image;   // image from computer
}
