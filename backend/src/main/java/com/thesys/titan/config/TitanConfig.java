package com.thesys.titan.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.titan")
public class TitanConfig {

    private String titanUrl;
    private String titanUsername;
    private String titanPassword;
    private String titanGraphName;
    private String titanGraphType;
    private String titanGraphVersion;
    private String titanGraphSchema;
    private String titanGraphSchemaVersion;

}
