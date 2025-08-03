package com.linreelle.saphir.dto;

import com.linreelle.saphir.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRequest {
    @NotBlank(message = "Firstname is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String firstName;
    private String middleName;
    @NotBlank(message = "Lastname is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String lastName;

    @NotBlank(message = "Password is required")
    @Size(min = 8)
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Address is required")
    private String address;
    private LocalDate dateOfBirth;

    private String title;

    @NotBlank(message = "Telephone number is required")
    @Size(max = 10, message = "Telephone number cannot exceed 10 characters")
    private String telephone;
    private Role role;

}
