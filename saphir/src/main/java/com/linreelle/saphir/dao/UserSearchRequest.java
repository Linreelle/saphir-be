package com.linreelle.saphir.dao;

import lombok.Data;

@Data
public class UserSearchRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String telephone;
    private String id;
}

