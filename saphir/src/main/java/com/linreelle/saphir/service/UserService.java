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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    private String getLoggedInUserName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
    }

    /**
     * Fetches the full user details and maps them to ProfileDto.
     */
    public ProfileDto getLoggedInUser() {
        String username = getLoggedInUserName();
        log.info("Get logged in user for username {}", username);
        if (username == null) {
            throw new IllegalStateException("No authenticated user found");
        }

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalStateException("No user found for profile: " + username));

        return ProfileMapper.toDTO(user);
    }


    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<UserResponse> getCustomers(){
        List<User> customers = userRepository.findAll();
        return customers.stream().map(UserMapper::toDTO).toList();
    }
    public List<EmployeeResponseDto> getUsers(){
        List<User> users = userRepository.findByIsUserTrue();
        return users.stream().map(UserMapper::toEmployeeDTO).toList();
    }
    public UserResponse getAUser(UUID id){
        User user = userRepository.findById(id).orElseThrow(
                ()-> new UserNotFindException("User not find with ID:" +id));

        return UserMapper.toDTO(user);

    }

    public UserResponse createUser (UserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExistsException("A User with this email already exists"
                    + request.getEmail());
        }
        else if (userRepository.existsByTelephone(request.getTelephone())){
            throw new TelephoneAlreadyExistException("A User with this telephone number already exists"
                    + request.getTelephone());
        }
        String name = getLoggedInUserName();

        User newUser = User.builder()
                .createdBy(name)
                .isUser(true)
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .telephone(request.getTelephone())
                .password(passwordEncoder.encode(request.getPassword()))
                .registeredDate(LocalDate.now())
                .title(request.getTitle())
                .address(request.getAddress())
                .dateOfBirth(request.getDateOfBirth())
                .role(request.getRole())
                .build();
                userRepository.save(newUser);
        return UserMapper.toDTO(newUser);
    }
    public AdhesionResponse adhesion (UUID id, AdhesionRequest request){
        User user = userRepository.findById(id).orElseThrow();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setMiddleName(request.getMiddleName());
        user.setTelephone(request.getTelephone());
        user.setEmail(request.getEmail());
        user.setIdType(request.getIdType());
        user.setIdCardNumber(request.getIdCardNumber());
        user.setIdCard(request.getIdCardBase64().getBytes());
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
        if (dto.getProfilePicture() != null && !dto.getProfilePicture().isEmpty()) {
            try {
                user.setProfilePicture(dto.getProfilePicture().getBytes());
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
