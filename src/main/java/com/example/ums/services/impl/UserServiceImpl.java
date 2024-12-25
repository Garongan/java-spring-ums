package com.example.ums.services.impl;

import com.example.ums.dto.user.UpdateUserRequest;
import com.example.ums.dto.user.UserRequest;
import com.example.ums.models.User;
import com.example.ums.repositories.UserRepository;
import com.example.ums.services.AuditLogService;
import com.example.ums.services.JwtService;
import com.example.ums.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;

    @Override
    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getUserByToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return getUserById(user.getId());
    }

    @Override
    public User createUser(UserRequest userRequest) {
        if (userRepository.existsUserByUsername(userRequest.username())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }

        if (userRepository.existsUserByEmail(userRequest.email())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        User user = User.builder()
                .name(userRequest.name())
                .username(userRequest.username())
                .email(userRequest.email())
                .roles(Set.of("ROLE USER"))
                .password(passwordEncoder.encode(userRequest.password()))
                .isEnable(false)
                .build();

        userRepository.save(user);
        auditLogService.log(user.getId(), "REGISTER USER");
        return user;
    }

    @Override
    public User updateUser(UpdateUserRequest updateUserRequest) {
        return null;
    }

    @Override
    public void disableUser(String id) {

    }

    @Override
    public void enableUser(String id) {

    }
}
