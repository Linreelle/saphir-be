package com.linreelle.saphir.dto;

import com.linreelle.saphir.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponseDto {
    private String id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String title;
    private String email;
    private String telephone;
    private String status;
    private Role role;
}
