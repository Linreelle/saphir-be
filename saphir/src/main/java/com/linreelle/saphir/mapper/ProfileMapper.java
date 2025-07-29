package com.linreelle.saphir.mapper;

import com.linreelle.saphir.dto.ChangeProfileDto;
import com.linreelle.saphir.dto.ProfileDto;
import com.linreelle.saphir.model.User;

public class ProfileMapper {
    public static ProfileDto toDTO(User user){
        ProfileDto response = new ProfileDto();

        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setProfile(user.getProfile());
        response.setEmail(user.getEmail());
        response.setTelephone(user.getTelephone());
        response.setDateOfBirth(user.getDateOfBirth());
        response.setAddress(user.getAddress());
        response.setPp(user.getPp());

        return response;
    }
    public static User toModel (ChangeProfileDto request){
        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setTelephone(request.getTelephone());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setAddress(request.getAddress());
        user.setPp(request.getPp());

        return user;
    }
}
