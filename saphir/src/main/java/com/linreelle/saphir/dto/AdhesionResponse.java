package com.linreelle.saphir.dto;


import com.linreelle.saphir.model.IdType;
import lombok.Data;

import java.util.UUID;

@Data
public class AdhesionResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String telephone;
    private String email;
    private IdType identityMeans;
    private String idCardNumber;
    private byte[] idCard;
    private String address;
}

