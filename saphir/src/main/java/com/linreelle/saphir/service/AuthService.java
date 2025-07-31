package com.linreelle.saphir.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linreelle.saphir.dto.LoginRequest;
import com.linreelle.saphir.dto.LoginResponse;
import com.linreelle.saphir.dto.RegisterRequest;
import com.linreelle.saphir.exception.EmailAlreadyExistsException;
import com.linreelle.saphir.exception.TelephoneAlreadyExistException;
import com.linreelle.saphir.model.Confirmation;
import com.linreelle.saphir.model.Token;
import com.linreelle.saphir.model.TokenType;
import com.linreelle.saphir.model.User;
import com.linreelle.saphir.repository.ConfirmationRepository;
import com.linreelle.saphir.repository.TokenRepository;
import com.linreelle.saphir.repository.UserRepository;
import com.linreelle.saphir.utils.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final ConfirmationRepository confirmationRepository;
    private final EmailService emailService;



    public Boolean verifyToken(String token) {
        Confirmation confirmation = confirmationRepository.findByToken(token);
        User user = userRepository.findByEmailIgnoreCase(confirmation.getUser().getEmail());
        user.setEnabled(true);
        user.setActive(true);
        userRepository.save(user);
        //confirmationRepository.delete(confirmation);
        return Boolean.TRUE;
    }
    public LoginResponse register(RegisterRequest request){
        if (userRepository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExistsException("A User with this email already exists"
                    + request.getEmail());
        }
        else if (userRepository.existsByTelephone(request.getTelephone())){
            throw new TelephoneAlreadyExistException("A User with this telephone number already exists"
                    + request.getTelephone());
        }

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .telephone(request.getTelephone())
                .password(passwordEncoder.encode(request.getPassword()))
                .confirmPassword(request.getConfirmPassword())
                .build();
        Confirmation confirmation = new Confirmation(user);
        try {
            emailService.sendSimpleMailMessage(user.getFirstName(), user.getEmail(), confirmation.getToken());

            user.setConfirmation(confirmation);
            var savedUser = userRepository.save(user);
            confirmationRepository.save(confirmation);
            var jwtToken = jwtUtil.generateToken(user);
            var refreshToken = jwtUtil.generateRefreshToken(user);
            saveUserToken(savedUser, jwtToken);
            return  LoginResponse.builder()
                    .access_token(jwtToken)
                    .refresh_token(refreshToken)
                    .build();

        } catch (MailException e){
            throw new RuntimeException(e);
        }

    }

    private void saveUserToken(User user, String jwtToken){
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }


    public LoginResponse authenticate(LoginRequest request){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!user.isEnabled()){
            throw new DisabledException("User is disabled");
        }
        var jwtToken = jwtUtil.generateToken(user);
        var refreshToken = jwtUtil.generateRefreshToken(user);

        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return LoginResponse.builder()
                .access_token(jwtToken)
                .refresh_token(refreshToken)
                .build();
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final  String refresh_token;
        final  String email;

        if(authHeader == null || !authHeader.startsWith("Bearer")){
            return;
        }
        refresh_token = authHeader.substring(7);
        email = jwtUtil.extractUsername(refresh_token);
        if (email != null){
            var user = this.userService.findByEmail(email)
                    .orElseThrow();
            if (jwtUtil.isTokenValid(refresh_token, user)){
                var access_token = jwtUtil.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, access_token);
                var authResponse = LoginResponse.builder()
                        .access_token(access_token)
                        .refresh_token(refresh_token)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(),authResponse);
            }
        }
    }

    public boolean validateToken(String token) {
        try {
            jwtUtil.validateToken(token);
            return true;
        } catch (JwtException e){
            return false;
        }
    }



    private void  revokeAllUserTokens(User user){
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());

        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}

