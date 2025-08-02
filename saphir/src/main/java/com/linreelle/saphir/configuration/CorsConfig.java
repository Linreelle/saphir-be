 package com.linreelle.saphir.configuration;

 import org.springframework.context.annotation.Bean;
 import org.springframework.context.annotation.Configuration;
 import org.springframework.web.servlet.config.annotation.CorsRegistry;
 import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

 @Configuration
 public class CorsConfig {
     @Bean
     public WebMvcConfigurer corsConfigurer() {
         return new WebMvcConfigurer() {
             @Override
             public void addCorsMappings (CorsRegistry registry) {
                WebMvcConfigurer.super.addCorsMappings(registry);
                registry.addMapping("/**")
                        .allowedOrigins("https://api-gateway-service.railway.internal",
                                "https://linreelle.github.io"
                                )
                        .allowedMethods("GET",  "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                        .allowCredentials(true)
                        .allowedHeaders("*");
             }
         };

     }

 }
