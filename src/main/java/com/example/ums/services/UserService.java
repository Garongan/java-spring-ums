package com.example.ums.services;

import com.example.ums.dto.user.UpdateUserRequest;
import com.example.ums.dto.user.UserRequest;
import com.example.ums.models.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User getUserById(String id);
    User getUserByToken();
    User createUser(UserRequest userRequest);
    User updateUser(UpdateUserRequest updateUserRequest);
    void disableUser(String id);
    void enableUser(String id);
}
