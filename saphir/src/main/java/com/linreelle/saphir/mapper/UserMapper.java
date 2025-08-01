package com.linreelle.saphir.mapper;

import com.linreelle.saphir.dto.AdhesionResponse;
import com.linreelle.saphir.dto.UserRequest;
import com.linreelle.saphir.dto.UserResponse;
import com.linreelle.saphir.model.User;

import java.time.LocalDate;

public class UserMapper {
    public static UserResponse toDTO(User user){
        UserResponse response = new UserResponse();

        response.setId(user.getId().toString());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setTelephone(user.getTelephone());

        System.out.println("Mapping user: " + user.getEmail());

        return response;
    }

    public static AdhesionResponse ToAdhesion (User user){
        AdhesionResponse response = new AdhesionResponse();

        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setMiddleName(user.getMiddleName());
        response.setEmail(user.getEmail());
        response.setTelephone(user.getTelephone());
        response.setIdentityMeans(user.getIdentityMeans());
        response.setIdCardNumber(user.getIdCardNumber());
        response.setIdCard(user.getIdCard());
        response.setAddress(user.getAddress());

        return response;

    }
    public static User toModel (UserRequest request){
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setTelephone(request.getTelephone());
        user.setConfirmPassword(request.getConfirmPassword());
        user.setAddress(request.getAddress());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setRegisteredDate(LocalDate.now());

        return user;
    }

}
