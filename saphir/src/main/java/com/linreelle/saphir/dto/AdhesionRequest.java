package com.linreelle.saphir.dto;


import com.linreelle.saphir.model.IdType;
import com.linreelle.saphir.model.services.Offer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

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
    private IdType idType;
    private String idCardNumber;
    private String idCardBase64;
    @NotBlank(message = "Address is required")
    private String address;

    private Set<Long> bundleIds;
    private  Set<Long> offerIds;

}

