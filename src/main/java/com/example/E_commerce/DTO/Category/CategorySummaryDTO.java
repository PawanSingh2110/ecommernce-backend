package com.example.E_commerce.DTO.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorySummaryDTO {

    private String name;

    private String slug;

    private String imageUrl;
}
