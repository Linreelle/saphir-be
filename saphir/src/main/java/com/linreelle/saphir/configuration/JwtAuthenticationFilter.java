package com.linreelle.saphir.configuration;

import com.linreelle.saphir.repository.TokenRepository;
import com.linreelle.saphir.utils.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        log.info("Request URI: {}", request.getRequestURI());

        if (request.getServletPath().contains("/api/v1/auth") &&
                !request.getServletPath().contains("/logout")){
            filterChain.doFilter(request, response);
            log.info("JWT filter running on: {}", request.getRequestURI());
            log.info("Authentication: {}", SecurityContextHolder.getContext().getAuthentication());
            return;
        }
        final String authHeader = request.getHeader("Authorization");
        log.debug("Authorization header: {}", authHeader);

        final String jwt;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        userEmail = jwtUtil.extractUsername(jwt);
        log.debug("Token received: {}", jwt);
        log.debug("User extracted: {}", userEmail);


        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            var isTokenValid = tokenRepository.findByToken(jwt)
                    .map(t-> !t.isExpired() && !t.isRevoked())
                    .orElse(false);
            if (jwtUtil.isTokenValid(jwt, userDetails) && isTokenValid){
                log.debug("Is token valid: {}", jwtUtil.isTokenValid(jwt, userDetails));
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                log.info("Authorities: {}", userDetails.getAuthorities());

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.debug("JWT filter set auth: {}", authToken);
                log.debug("Setting SecurityContext with user: {}", userDetails.getUsername());


            }
        }
        filterChain.doFilter(request, response);

    }
}