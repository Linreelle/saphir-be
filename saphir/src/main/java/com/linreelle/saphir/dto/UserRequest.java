package com.linreelle.saphir.dto;

import com.linreelle.saphir.dto.validators.CreateUserValidationGroup;
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

    @NotBlank(message = "Lastname is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String lastName;

    @NotBlank(message = "Password is required")
    @Size(min = 8)
    private String password;

    private String confirmPassword;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotBlank(groups = CreateUserValidationGroup.class, message = "Registered date is required")
    private LocalDate registeredDate;

    @NotBlank(message = "Telephone number is required")
    @Size(max = 10, message = "Telephone number cannot exceed 10 characters")
    private String telephone;

    @NotBlank
    private Role role;

}
