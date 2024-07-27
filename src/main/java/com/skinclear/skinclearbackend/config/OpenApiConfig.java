package com.skinclear.skinclearbackend.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SkinClear API")
                        .version("1.0")
                        .description("API documentation for the SkinClear project")
                        .contact(new Contact()
                                .name("Skin Clear")
                                .url("www.skinclear.com")
                                .email("support@skinclear.com")));
    }
}