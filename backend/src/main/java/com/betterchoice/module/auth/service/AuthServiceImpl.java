package com.betterchoice.module.auth.service;

import com.betterchoice.core.exception.BusinessException;
import com.betterchoice.core.security.JwtProvider;
import com.betterchoice.module.auth.dto.request.LoginRequest;
import com.betterchoice.module.auth.dto.request.RegisterRequest;
import com.betterchoice.module.auth.dto.response.AuthResponse;
import com.betterchoice.module.auth.entity.RefreshToken;
import com.betterchoice.module.auth.repository.RefreshTokenRepository;
import com.betterchoice.module.user.dto.response.UserResponse;
import com.betterchoice.module.user.entity.User;
import com.betterchoice.module.user.repository.UserRepository;
import com.betterchoice.shared.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("EMAIL_EXISTS", "Email is already registered", HttpStatus.CONFLICT);
        }

        User user = User.builder()
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);
        return buildAuthResponse(user);
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new BusinessException("ACCOUNT_DISABLED", "Account is disabled", HttpStatus.FORBIDDEN);
        }

        return buildAuthResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse refreshToken(String refreshToken) {
        RefreshToken storedToken = validateRefreshToken(refreshToken);
        User user = storedToken.getUser();

        return new AuthResponse(
                jwtProvider.generateAccessToken(user),
                refreshToken,
                jwtProvider.getAccessTokenExpirySeconds(),
                toUserResponse(user)
        );
    }

    @Override
    @Transactional
    public void logout(String refreshToken) {
        String tokenHash = hashToken(refreshToken);
        refreshTokenRepository.findByTokenHashAndRevokedFalse(tokenHash)
                .ifPresent(token -> token.setRevoked(true));
    }

    private AuthResponse buildAuthResponse(User user) {
        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);
        persistRefreshToken(user, refreshToken);

        return new AuthResponse(
                accessToken,
                refreshToken,
                jwtProvider.getAccessTokenExpirySeconds(),
                toUserResponse(user)
        );
    }

    private void persistRefreshToken(User user, String refreshToken) {
        Claims claims = jwtProvider.parseRefreshToken(refreshToken);

        RefreshToken entity = RefreshToken.builder()
                .user(user)
                .tokenHash(hashToken(refreshToken))
                .expiresAt(jwtProvider.getExpiration(claims))
                .build();

        refreshTokenRepository.save(entity);
    }

    private RefreshToken validateRefreshToken(String refreshToken) {
        try {
            jwtProvider.parseRefreshToken(refreshToken);
        } catch (JwtException ex) {
            throw new BusinessException("INVALID_REFRESH_TOKEN", "Invalid refresh token", HttpStatus.UNAUTHORIZED);
        }

        return refreshTokenRepository.findByTokenHashAndRevokedFalse(hashToken(refreshToken))
                .filter(token -> token.getExpiresAt().isAfter(Instant.now()))
                .orElseThrow(() -> new BusinessException(
                        "INVALID_REFRESH_TOKEN", "Invalid refresh token", HttpStatus.UNAUTHORIZED));
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole(),
                user.getAvatarUrl()
        );
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 algorithm not available", ex);
        }
    }
}
