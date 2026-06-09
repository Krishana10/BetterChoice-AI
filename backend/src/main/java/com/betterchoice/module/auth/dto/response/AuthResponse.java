package com.betterchoice.module.auth.dto.response;

import com.betterchoice.module.user.dto.response.UserResponse;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        long expiresIn,
        UserResponse user
) {}
