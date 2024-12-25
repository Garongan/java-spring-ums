package com.example.ums.dto.user;

import java.util.List;

public record UserResponse(String id, String name, String username, String email, List<String> roles) {
}
