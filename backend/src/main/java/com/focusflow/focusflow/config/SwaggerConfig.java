// File: com.focusflow.focusflow.config.SwaggerConfig.java

package com.focusflow.focusflow.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI focusFlowOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FocusFlow API")
                        .description("API documentation for your To-Do App")
                        .version("1.0.0"));
    }
}
