package com.betterchoice.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "betterchoice.jwt")
public class JwtProperties {

    private String secret = "change-me-in-production-use-256-bit-key-minimum";
    private long accessTokenExpiry = 900;
    private long refreshTokenExpiry = 604800;
}
