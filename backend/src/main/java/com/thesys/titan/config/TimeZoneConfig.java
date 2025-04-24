package com.thesys.titan.config;

import java.util.TimeZone;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class TimeZoneConfig implements InitializingBean {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Override
    public void afterPropertiesSet() {
        log.info("Active profile: {}", activeProfile);
        if (!activeProfile.equals("local")) {
            System.setProperty("user.timezone", "Asia/Seoul");
            TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
            log.info("Set timezone to Asia/Seoul");
        }
    }
}
