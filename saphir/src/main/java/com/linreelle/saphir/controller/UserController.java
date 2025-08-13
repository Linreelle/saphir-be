package com.linreelle.saphir.controller;


import com.linreelle.saphir.dao.UserSearchRequest;
import com.linreelle.saphir.dto.*;
import com.linreelle.saphir.dto.validators.CreateUserValidationGroup;
import com.linreelle.saphir.model.User;
import com.linreelle.saphir.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@EnableMethodSecurity
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    @InitBinder
    public void initBinder(WebDataBinder binder){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

    @GetMapping("/getProfile")
    public ResponseEntity<ProfileDto> profile(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        log.info("Get logged in user for username {}", username);
        log.debug("Get logged in user for username {}", username);

        ProfileDto response = userService.getProfileByUsername(username);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get Customers")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<List<UserResponse>> getCustomers(){
            List<UserResponse> customers = userService.getCustomers();
            return ResponseEntity.ok().body(customers);
      }

      @GetMapping("/systemUsers")
      @Operation(summary = "Get users")
      @PreAuthorize("hasRole('ADMIN')")
      public ResponseEntity<List<EmployeeResponseDto>> getUsers(Pageable pageable){
        List<EmployeeResponseDto> users = userService.getUsers(pageable);
        return ResponseEntity.ok().body(users);
      }

    @GetMapping("/{id}")
    @Operation(summary = "Get a user")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<UserResponse> getAUser(
            @PathVariable UUID id){
        UserResponse user = userService.getAUser(id);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping(value = {"/criteria/{userSearchRequest}"})
    @Operation(summary = "Get users by criteria")
    @PreAuthorize("hasAnyRole('ADMIN','USER','MANAGER')")
    public List<User> searchByCriteria(@PathVariable UserSearchRequest userSearchRequest){
        return userService.findByCriteria(userSearchRequest);
    }

    @PostMapping
    @Operation(summary = "Create a new user")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<UserResponse> createUser(
            @Validated({Default.class, CreateUserValidationGroup.class})
            @RequestBody UserRequest request,
            ModelMap modelMap) {

        UserResponse userResponseDTO = userService.createUser(request);

        return ResponseEntity.ok().body(userResponseDTO);
    }
    @PatchMapping("/adhesion/{id}")
    @Operation(summary = "Subscribe to an offer")
    public ResponseEntity<AdhesionResponse> adhesion(
            @PathVariable UUID id, @Validated({Default.class})
            @RequestBody AdhesionRequest request
    ){
        AdhesionResponse response = userService.adhesion(id, request);
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/profile/change")
    public ResponseEntity<ProfileDto> changeProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @ModelAttribute ChangeProfileDto dto
    ) {
        String username = userDetails.getUsername();
        ProfileDto updatedProfile = userService.changeProfile(username, dto);
        return ResponseEntity.ok(updatedProfile);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update an existing user")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable UUID id, @Validated({Default.class})
            @RequestBody UserRequest userRequestDTO){

        UserResponse userResponseDTO = userService.updateUser(id, userRequestDTO);

        return ResponseEntity.ok().body(userResponseDTO);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete a user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id){
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    private UUID getLoggedInUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            return ((User) principal).getId();
        }
        return (UUID) principal;
    }
}

