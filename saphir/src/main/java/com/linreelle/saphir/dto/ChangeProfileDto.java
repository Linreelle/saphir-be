package com.linreelle.saphir.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class ChangeProfileDto {
        private String firstName;
        private String lastName;
        private String email;
        private String telephone;
        private LocalDate dateOfBirth;
        private String address;
        private MultipartFile  pp;
}
