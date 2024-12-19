package com.example.ums.services.impl;

import com.example.ums.models.User;
import com.example.ums.repositories.UserRepository;
import com.example.ums.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }
}
