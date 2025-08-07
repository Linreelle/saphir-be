package com.linreelle.saphir.dto;


import com.linreelle.saphir.model.IdType;
import lombok.Data;

@Data
public class AdhesionResponse {
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

