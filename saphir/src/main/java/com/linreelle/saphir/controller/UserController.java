package com.linreelle.saphir.controller;


import com.linreelle.saphir.dao.UserSearchRequest;
import com.linreelle.saphir.dto.*;
import com.linreelle.saphir.dto.validators.CreateUserValidationGroup;
import com.linreelle.saphir.model.User;
import com.linreelle.saphir.repository.UserRepository;
import com.linreelle.saphir.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final UserRepository userRepository;
    @InitBinder
    public void initBinder(WebDataBinder binder){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }
    private String getLoggedInUser(ModelMap modelMap){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User){
            return ((User) principal).getFirstName() + " " + ((User) principal).getLastName();
        }
        return principal.toString();
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileDto> profile(ModelMap modelMap){
        ProfileDto response = userService.getLoggedInUser(modelMap);
        return ResponseEntity.ok(response);
    }


    @GetMapping
    @Operation(summary = "Get Customers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getCustomers(){
            List<UserResponse> customers = userService.getCustomers();
            return ResponseEntity.ok().body(customers);
      }

      @GetMapping("/systemUsers")
      @Operation(summary = "Get users")
      @PreAuthorize("hasRole('ADMIN')")
      public ResponseEntity<List<EmployeeResponseDto>> getUsers(){
        List<EmployeeResponseDto> users = userService.getUsers();
        return ResponseEntity.ok().body(users);
      }
//    @GetMapping
//    public ResponseEntity<?> getAllUsers( ModelMap modelMap) {
//        log.info("Attempting to log in using request {}", getLoggedInUser(modelMap));
//        try {
//            List<User> users = userRepository.findAll();
//            System.out.println("Fetched users: " + users);
//
//            return ResponseEntity.ok(users);
//
//        } catch (Exception e) {
//            log.error("Login failed: {} ",e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error fetching users: " + e.getMessage());
//        }
//    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a user")
    public ResponseEntity<UserResponse> getAUser(
            @PathVariable UUID id){
        UserResponse user = userService.getAUser(id);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping(value = {"/criteria/{userSearchRequest}"})
    @Operation(summary = "Get users by criteria")
    public List<User> searchByCriteria(@PathVariable UserSearchRequest userSearchRequest){
        return userService.findByCriteria(userSearchRequest);
    }

    @PostMapping
    @Operation(summary = "Create a new user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> createUser(
            @Validated({Default.class, CreateUserValidationGroup.class})
            @RequestBody UserRequest request,
            ModelMap modelMap) {

        UserResponse userResponseDTO = userService.createUser(request, modelMap);

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

    @PutMapping("/profile")
    public ResponseEntity<ProfileDto> updateProfile(
            @ModelAttribute ChangeProfileDto dto,
            Authentication authentication
    ) {
        ProfileDto updatedProfile = userService.updateLoggedInUser(dto, authentication);
        return ResponseEntity.ok(updatedProfile);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update an existing user")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable UUID id, @Validated({Default.class})
            @RequestBody UserRequest userRequestDTO){

        UserResponse userResponseDTO = userService.updateUser(id, userRequestDTO);

        return ResponseEntity.ok().body(userResponseDTO);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete a user")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id){
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}

