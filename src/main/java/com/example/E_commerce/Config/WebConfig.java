package com.example.E_commerce.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 🔥 CHANGE 5: ABSOLUTE PATH (matches service)
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
        // WAS: "file:uploads/"
    }
}
