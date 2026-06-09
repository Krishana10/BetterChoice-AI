package com.betterchoice.module.auth.service;

import com.betterchoice.module.auth.dto.request.LoginRequest;
import com.betterchoice.module.auth.dto.request.RegisterRequest;
import com.betterchoice.module.auth.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(String refreshToken);

    void logout(String refreshToken);
}
