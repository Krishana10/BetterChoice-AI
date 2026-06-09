package com.betterchoice.module.user.dto.response;

import com.betterchoice.shared.enums.Role;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        String fullName,
        Role role,
        String avatarUrl
) {}
