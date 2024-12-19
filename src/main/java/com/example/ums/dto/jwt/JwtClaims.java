package com.example.ums.dto.jwt;

import java.util.List;

public record JwtClaims(String userId, List<String> roles) {
}
