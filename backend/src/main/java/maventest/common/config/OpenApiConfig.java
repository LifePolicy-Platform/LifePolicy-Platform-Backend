package maventest.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    public static final String BEARER_SCHEME = "bearerAuth";

    @Bean
    public OpenAPI appOpenApi() {
        return new OpenAPI()
            .info(new Info()
                .title("Change Appointment Time API")
                .description("JWT-protected API. Login via /api/v1/auth/login, then call protected APIs with Authorization: Bearer <token>.")
                .version("v1"))
            .components(new Components().addSecuritySchemes(BEARER_SCHEME,
                new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("Paste JWT access token returned by /api/v1/auth/login.")));
    }
}
