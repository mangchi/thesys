package com.thesys.titan.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Bean
    public OpenAPI openAPI() {
        if (activeProfile.equals("dev")) {
            return new OpenAPI().addSecurityItem(new SecurityRequirement().addList("JWT"))
                    .components(new Components().addSecuritySchemes("JWT", createAPIKeyScheme()))
                    .info(apiInfo());
        }
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Titan API 명세서")
                .description("TheSys Titan API 명세서")
                .version("1.0.0");
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .name("Authorization")
                .bearerFormat("JWT")
                .scheme("bearer");
    }
}