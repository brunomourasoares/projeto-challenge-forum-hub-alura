package alura.forumhub.api.infra.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.application.version}")
    private String appVersion;

    @Value("${spring.application.email}")
    private String appEmail;

    @Value("${spring.application.website}")
    private String appWebsite;

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "Bearer Authentication";

        return new OpenAPI()
            .info(new Info()
                .title(appName + " API")
                .description("API do Fórum Hub, uma plataforma para discussões e compartilhamento de conhecimento.")
                .version(appVersion)
                .contact(new Contact()
                    .name("Equipe de Desenvolvimento")
                    .email(appEmail)
                    .url(appWebsite))
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
            .servers(List.of(
                new Server()
                    .url("http://localhost:8080")
                    .description("Servidor de Desenvolvimento"),
                new Server()
                    .url("https://api.alura.forumhub")
                    .description("Servidor de Produção")))
            .addSecurityItem(new SecurityRequirement()
                .addList(securitySchemeName))
            .components(new Components()
                .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                    .name(securitySchemeName)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("Insira o token JWT obtido no login. Exemplo: `eyJhbGciASAs11AS2asHHsashgbmm176asgzzjhf...`")));
    }
}