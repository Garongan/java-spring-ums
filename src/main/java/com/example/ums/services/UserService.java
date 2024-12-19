package com.example.ums.services;

import com.example.ums.models.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User getUserById(String id);
}
