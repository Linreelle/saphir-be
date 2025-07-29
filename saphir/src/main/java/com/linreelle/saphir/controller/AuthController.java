package com.linreelle.saphir.controller;


import com.linreelle.saphir.dto.LoginRequest;
import com.linreelle.saphir.dto.LoginResponse;
import com.linreelle.saphir.dto.RegisterRequest;
import com.linreelle.saphir.model.User;
import com.linreelle.saphir.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    private String getLoggedInUser(ModelMap modelMap){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User){
            return ((User) principal).getFirstName() + " " + ((User) principal).getLastName();
        }
        return principal.toString();
    }
    @Operation(summary = "Generate token on login")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        log.info("Attempting to login using request {}", request.getEmail());
        try {
            return ResponseEntity.ok(authService.authenticate(request));
        } catch (Exception e) {
            log.error("Login failed: {} ",e.getMessage());
            ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody RegisterRequest request){
        log.info("Attempting to register using request {}", request.getEmail());
        try {
            return ResponseEntity.ok(authService.register(request));
        } catch (Exception e) {
            log.error("Register failed: {} ",e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

    @GetMapping("/register")
    public ResponseEntity<Boolean> verifyToken(@RequestParam("token") String token){
        return ResponseEntity.ok(authService.verifyToken(token));
    }

    @Operation(summary = "Validate Token")
    @GetMapping("/validate")
    public ResponseEntity<Void> validateToken(
            @RequestHeader("Authorization") String authHeader){
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return authService.validateToken(authHeader.substring(7))
                ? ResponseEntity.ok().build()
                :ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/refresh_token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authService.refreshToken(request, response);
    }

//    @PostMapping("/logout")
//    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
//        logoutService.logout(request, response, authentication);
//    }

}
