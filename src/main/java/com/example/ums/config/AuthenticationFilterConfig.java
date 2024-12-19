package com.example.ums.config;

import com.example.ums.dto.jwt.JwtClaims;
import com.example.ums.services.JwtService;
import com.example.ums.models.User;
import com.example.ums.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class AuthenticationFilterConfig extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;
    final String AUTH_HEADER = "Authorization";

    @Override
    protected void doFilterInternal(
            @NonNull
            HttpServletRequest request,
            @NonNull
            HttpServletResponse response,
            @NonNull
            FilterChain filterChain) throws ServletException, IOException {
        try {
            String bearerToken = request.getHeader(AUTH_HEADER);
            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                JwtClaims claimsByToken = jwtService.getClaimsByToken(bearerToken);
                User userById = userService.getUserById(claimsByToken.userId());
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userById.getUsername(),
                        null,
                        userById.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
        filterChain.doFilter(request, response);
    }
}
