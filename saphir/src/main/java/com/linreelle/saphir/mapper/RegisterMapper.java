package com.linreelle.saphir.mapper;

import com.linreelle.saphir.dto.RegisterRequest;
import com.linreelle.saphir.model.User;

public class RegisterMapper {
    public static User toModel (RegisterRequest request){

        User user = new User();

        user.setLastName(request.getLastName());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setTelephone(request.getTelephone());
        user.setConfirmPassword(request.getConfirmPassword());

        return user;
    }
}


