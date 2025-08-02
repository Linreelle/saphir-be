package com.linreelle.saphir.controller;


import com.linreelle.saphir.service.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/logout")
@CrossOrigin(origins = {
        "https://api-gateway-service.railway.internal",
        "https://linreelle.github.io"
}, methods = {
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.PUT,
        RequestMethod.DELETE,
        RequestMethod.OPTIONS,
        RequestMethod.PATCH,
        RequestMethod.HEAD
})
public class LogoutController {
    private final LogoutService logoutService;

    @PostMapping
    public ResponseEntity<?> logout(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Authentication authentication) {

        System.out.println("Logout URL: /logoutUrl ");
        logoutService.logout(request, response, authentication);
        return ResponseEntity.ok("Logout successful.");
    }
}
