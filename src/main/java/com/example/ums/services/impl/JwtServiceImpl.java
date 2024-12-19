package com.example.ums.services.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.ums.dto.jwt.JwtClaims;
import com.example.ums.services.JwtService;
import com.example.ums.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
public class JwtServiceImpl implements JwtService {
    private final String JWT_SECRET;
    private final String JWT_ISSUER;
    private final long JWT_EXPIRATION;

    public JwtServiceImpl(
            @Value("${jwt.secret}")
            String jwtSecret,
            @Value("${jwt.issuer}")
            String jwtIssuer,
            @Value("${jwt.expiration}")
            long jwtExpiration) {
        JWT_SECRET = jwtSecret;
        JWT_ISSUER = jwtIssuer;
        JWT_EXPIRATION = jwtExpiration;
    }

    @Override
    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(JWT_SECRET);
            return JWT.create()
                    .withSubject(user.getId())
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(Instant.now().plusSeconds(JWT_EXPIRATION))
                    .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                    .withIssuer(JWT_ISSUER)
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public boolean verifyJwtToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(JWT_SECRET);
            algorithm.verify(JWT.require(algorithm).build().verify(parseToken(token)));
            return true;
        } catch (JWTCreationException e) {
            return false;
        }
    }

    @Override
    public JwtClaims getClaimsByToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(JWT_SECRET);
            DecodedJWT decodedJWT = JWT.require(algorithm).withIssuer(JWT_ISSUER).build().verify(parseToken(token));
            return new JwtClaims(decodedJWT.getSubject(), decodedJWT.getClaim("roles").asList(String.class));
        } catch (JWTCreationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private String parseToken(String bearerToken) {
        return bearerToken.replace("Bearer ", "");
    }
}
