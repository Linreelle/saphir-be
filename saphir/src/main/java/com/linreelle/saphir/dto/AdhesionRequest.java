package com.linreelle.saphir.dto;


import com.linreelle.saphir.model.IdentityMeans;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AdhesionRequest {
    @NotBlank(message = "Firstname is required")
    private String firstName;
    @NotBlank(message = "Lastname is required")
    private String lastName;
    private String middleName;
    @NotBlank(message = "Telephone number is required")
    @Size(min = 10, max = 13)
    private String telephone;
    @NotBlank(message = "Email address is required")
    @Email
    private String email;
    private IdentityMeans identityMeans;
    private String idCardNumber;
    private byte[] idCard;
    @NotBlank(message = "Address is required")
    private String address;
}

