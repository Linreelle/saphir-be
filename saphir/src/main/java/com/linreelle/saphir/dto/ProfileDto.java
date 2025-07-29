package com.linreelle.saphir.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileDto {
    private String firstName;
    private String lastName;
    private String profile;
    private String email;
    private String telephone;
    private LocalDate dateOfBirth;
    private String address;
    private byte[] pp;
}
