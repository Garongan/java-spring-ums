package com.example.ums.services;

import com.example.ums.dto.jwt.JwtClaims;
import com.example.ums.models.User;
import org.springframework.stereotype.Service;

@Service
public interface JwtService {
    String generateToken(User user);
    boolean verifyJwtToken(String token);
    JwtClaims getClaimsByToken(String token);
}
