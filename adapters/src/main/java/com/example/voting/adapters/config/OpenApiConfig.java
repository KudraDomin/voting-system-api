package com.example.voting.adapters.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    private static final String API_TITLE = "Voting System API";

    private static final String API_VERSION = "1.0.0";

    private static final String API_DESCRIPTION =
            "REST API for registering votes across multiple independent elections";

    @Bean
    public OpenAPI votingSystemOpenApi() {
        return new OpenAPI().info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title(API_TITLE)
                .version(API_VERSION)
                .description(API_DESCRIPTION)
                .contact(apiContact())
                .license(apiLicense());
    }

    private Contact apiContact() {
        return new Contact().name("Voting System Team");
    }

    private License apiLicense() {
        return new License().name("Apache-2.0").url("https://www.apache.org/licenses/LICENSE-2.0");
    }
}
