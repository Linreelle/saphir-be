package com.linreelle.saphir.mapper;

import com.linreelle.saphir.dto.ProfileDto;
import com.linreelle.saphir.model.User;

import java.util.Base64;

public class ProfileMapper {
    public static ProfileDto toDTO(User user) {
        String base64Image = null;
        if (user.getProfilePicture() != null && user.getProfilePicture().length > 0) {
            base64Image = Base64.getEncoder().encodeToString(user.getProfilePicture());
        }
        return ProfileDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .profile(user.getProfile())
                .email(user.getEmail())
                .telephone(user.getTelephone())
                .dateOfBirth(user.getDateOfBirth())
                .address(user.getAddress())
                .profilePictureBase64(base64Image)
                .build();
    }
}

