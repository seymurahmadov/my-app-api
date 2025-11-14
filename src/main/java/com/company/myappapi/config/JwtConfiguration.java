package com.company.myappapi.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Getter
public class JwtConfiguration {
    @Value("${security.jwt.expiration:#{8*60*60*1000}}")
    private int expiration;

    @Value("${jwt.secret}")
    private String secret;

    @Value("#{'${app.allowed.origins}'.split(',')}")
    private List<String> allowedOrigins;
}
