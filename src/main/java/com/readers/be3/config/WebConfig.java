package com.readers.be3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*");
    }
    @Bean
    public OpenAPI readersOpenAPI() {
        Info info = new Info().version("0.0.1").title("독서일정관리서비스 Readers API").description("독서일정관리서비스 Readers Restful API 명세서");
        return new OpenAPI().info(info);
    }
}
