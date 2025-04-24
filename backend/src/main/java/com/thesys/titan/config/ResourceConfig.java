package com.thesys.titan.config;

// import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {

        // @Value("${spring.servlet.multipart.location}")
        // private String uploadPath;

        @Override
        public void addResourceHandlers(
                        org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/favicon.ico")
                                .addResourceLocations("classpath:/static/");

                registry.addResourceHandler("/css/**")
                                .addResourceLocations("classpath:/static/css/");
                registry.addResourceHandler("/image/**")
                                .addResourceLocations("classpath:/static/image/");
                registry.addResourceHandler("/img/**")
                                .addResourceLocations("file:///" + "/upload/");
                // .addResourceLocations("file:///" + uploadPath + "/");
        }

}