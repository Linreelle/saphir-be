package com.linreelle.saphir.configuration;

import com.linreelle.saphir.service.LogoutService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.linreelle.saphir.model.Permission.*;
import static com.linreelle.saphir.model.Role.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutService logoutService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .addLogoutHandler(logoutService)  // Inject your service here
                        .logoutSuccessHandler((request, response, authentication) ->
                                response.setStatus(HttpServletResponse.SC_OK)
                        ))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(
                        auth ->
                                auth
                                        .requestMatchers("/login", "/register", "/error", "/").permitAll()

                                        .requestMatchers("/logout").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/logout").hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name(), USER.name())

                                        .requestMatchers("/users/**").hasAnyRole(ADMIN.name(), MANAGER.name(), USER.name())
                                        .requestMatchers(HttpMethod.GET, "/users").hasAuthority(ADMIN_READ.name())
                                        .requestMatchers(HttpMethod.GET, "/users/profile").hasAuthority(USER_READ.name())
                                        .requestMatchers(HttpMethod.PUT, "/users/profile").hasAuthority(USER_UPDATE.name())
                                        .requestMatchers(HttpMethod.GET, "/users/**").hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name())
                                        .requestMatchers(HttpMethod.POST, "/users/**").hasAuthority(ADMIN_CREATE.name())
                                        .requestMatchers(HttpMethod.PUT, "/users/**").hasAuthority(ADMIN_UPDATE.name())
                                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasAuthority(ADMIN_DELETE.name())

                                        .requestMatchers("/bundles/**").hasAnyRole(ADMIN.name(), MANAGER.name(), USER.name())
                                        .requestMatchers(HttpMethod.GET, "/bundles").hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name(), USER_READ.name())
                                        .requestMatchers(HttpMethod.GET, "/bundles/**").hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name(), USER_READ.name())
                                        .requestMatchers(HttpMethod.POST, "/bundles").hasAnyAuthority(ADMIN_CREATE.name(), MANAGER_READ.name())
                                        .requestMatchers(HttpMethod.PATCH, "/bundles/**").hasAnyAuthority(ADMIN_UPDATE.name(), MANAGER_UPDATE.name())
                                        .requestMatchers(HttpMethod.DELETE, "/bundles/**").hasAuthority(ADMIN_DELETE.name())

                                        .requestMatchers("/offers/**").hasAnyRole(ADMIN.name(), MANAGER.name(), USER.name())
                                        .requestMatchers(HttpMethod.GET, "/offers").hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name(), USER_READ.name())
                                        .requestMatchers(HttpMethod.GET, "/offers/**").hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name(), USER_READ.name())
                                        .requestMatchers(HttpMethod.POST, "/offers").hasAnyAuthority(ADMIN_CREATE.name(), MANAGER_READ.name())
                                        .requestMatchers(HttpMethod.POST, "/offers/**").hasAnyAuthority(USER_CREATE.name())
                                        .requestMatchers(HttpMethod.PUT, "/offers/**").hasAnyAuthority(ADMIN_UPDATE.name(), MANAGER_UPDATE.name())
                                        .requestMatchers(HttpMethod.DELETE, "/offers/**").hasAuthority(ADMIN_DELETE.name())

                                        .anyRequest().authenticated()
                )
                .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        ;


        return http.build();
    }

}
