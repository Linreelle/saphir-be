package com.linreelle.saphir;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SaphirApplication {
    public static void main(String[] args) {
        System.out.println("Java version: " + System.getProperty("java.version"));
        SpringApplication.run(SaphirApplication.class, args);
    }

}
