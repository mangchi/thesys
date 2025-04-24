package com.thesys.titan.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties
public class VaultConfig {

    // private String dbName;
    // private String dbHostName;
    // private String secondaryDbName;
    // private String secondaryDbHostName;
    // private String port;
    private String dbUser;
    private String userPwd;
    // private String jwtSecret;
    // private String tokenExpired;

}
