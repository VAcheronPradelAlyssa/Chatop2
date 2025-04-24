package com.openclassrooms.chatop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Sert les fichiers depuis "uploads/" comme /uploads/filename.jpg
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + Paths.get("uploads").toAbsolutePath().toUri());
    }
}
