package com.linreelle.saphir.service;

import com.linreelle.saphir.dao.UserDao;
import com.linreelle.saphir.dao.UserSearchRequest;
import com.linreelle.saphir.dto.*;
import com.linreelle.saphir.exception.EmailAlreadyExistsException;
import com.linreelle.saphir.exception.TelephoneAlreadyExistException;
import com.linreelle.saphir.exception.UserNotFindException;
import com.linreelle.saphir.mapper.ProfileMapper;
import com.linreelle.saphir.mapper.UserMapper;
import com.linreelle.saphir.model.User;
import com.linreelle.saphir.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserDao userDao;

    private String getLoggedInUserName(ModelMap modelMap){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User){
            return ((User) principal).getEmail();
        }
        return principal.toString();
    }

    // If you want to return your full custom User entity from the database:
    public ProfileDto getLoggedInUser(ModelMap modelMap) {
        String name = getLoggedInUserName(modelMap);
        if (name == null) {
            throw new IllegalStateException("No profile found in ModelMap");
        }
        User user = userRepository.findByEmail(name)
                .orElseThrow(() -> new IllegalStateException("No user found for profile: " + name));
        return ProfileMapper.toDTO(user);
    }


    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<UserResponse> getUsers(){
        List<User> users = userRepository.findAll();
        return users.stream().map(UserMapper::toDTO).toList();
    }

    public UserResponse getAUser(UUID id){
        User user = userRepository.findById(id).orElseThrow(
                ()-> new UserNotFindException("User not find with ID:" +id));

        return UserMapper.toDTO(user);
    }

    public UserResponse createUser (UserRequest request){
        if (userRepository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExistsException("A User with this email already exists"
                    + request.getEmail());
        }
        else if (userRepository.existsByTelephone(request.getTelephone())){
            throw new TelephoneAlreadyExistException("A User with this telephone number already exists"
                    + request.getTelephone());
        }
        User newCustomer = userRepository.save(
                UserMapper.toModel(request));

        return UserMapper.toDTO(newCustomer);
    }
    public AdhesionResponse adhesion (UUID id, AdhesionRequest request){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFindException("User not find with ID:" +id));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setMiddleName(request.getMiddleName());
        user.setTelephone(request.getTelephone());
        user.setEmail(request.getEmail());
        user.setIdentityMeans(request.getIdentityMeans());
        user.setIdCardNumber(request.getIdCardNumber());
        user.setIdCard(request.getIdCard());
        user.setAddress(request.getAddress());

        User subscribedUser = userRepository.save(user);

        return UserMapper.ToAdhesion(subscribedUser);
    }
    public ProfileDto updateLoggedInUser(ChangeProfileDto dto, Authentication authentication) {
        String name = authentication.getName();
        User user = userRepository.findByEmail(name)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with email:" +name));
        // Update profile fields
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setTelephone(dto.getTelephone());
        user.setDateOfBirth(dto.getDateOfBirth()); // if date is a String
        user.setAddress(dto.getAddress());

        // Update profile picture only if file was uploaded
        if (dto.getPp() != null && !dto.getPp().isEmpty()) {
            try {
                user.setPp(dto.getPp().getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to process profile picture", e);
            }
        }


        User updated = userRepository.save(user);
        return ProfileMapper.toDTO(updated);
    }



    public UserResponse updateUser(UUID id, UserRequest request){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFindException("User not find with ID:" +id));

        if (userRepository.existsByEmailAndIdNot(request.getEmail(), id)){
            throw new EmailAlreadyExistsException("A user with this email already exists"
                    + request.getEmail());
        }
        else if (userRepository.existsByTelephoneAndIdNot(request.getTelephone(), id)){
            throw new TelephoneAlreadyExistException("A user with this telephone number already exists"
                    + request.getTelephone());
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setAddress(request.getAddress());
        user.setDateOfBirth((request.getDateOfBirth()));
        user.setTelephone(request.getTelephone());
        user.setRole(request.getRole());

        User updatedUser = userRepository.save(user);

        return UserMapper.toDTO(updatedUser);
    }

    public void deleteUser(UUID id){
        userRepository.deleteById(id);
    }

    public List<User> findByCriteria(UserSearchRequest userSearchRequest){
        return userDao.searchByCriteria(userSearchRequest);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
