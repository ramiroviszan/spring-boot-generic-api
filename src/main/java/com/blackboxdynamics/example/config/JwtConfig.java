package com.blackboxdynamics.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@Data
@ConfigurationProperties(prefix = "application.jwt")
@ConfigurationPropertiesScan
public class JwtConfig {

    private String secret;
    private String tokenPrefix;
    private String issuer;
    private int expirationDays;
}
