package com.linreelle.saphir.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String profile;
    private String email;
    private String telephone;
    private LocalDate dateOfBirth;
    private String address;
    private String profilePictureBase64;
}

