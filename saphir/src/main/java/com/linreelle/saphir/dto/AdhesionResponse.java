package com.linreelle.saphir.dto;


import com.linreelle.saphir.model.IdentityMeans;
import lombok.Data;

@Data
public class AdhesionResponse {
    private String firstName;
    private String lastName;
    private String middleName;
    private String telephone;
    private String email;
    private IdentityMeans identityMeans;
    private String idCardNumber;
    private byte[] idCard;
    private String address;
}

