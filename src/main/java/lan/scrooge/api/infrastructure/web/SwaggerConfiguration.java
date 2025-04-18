package lan.scrooge.api.infrastructure.web;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    servers = @Server(url = "/"),
    info = @Info(title = "Scrooge Be", version = "1.0.0"),
    security = {@SecurityRequirement(name = "auth")})
@SecurityScheme(
    type = SecuritySchemeType.OAUTH2,
    name = "Keycloak",
    scheme = "bearer",
    bearerFormat = "JWT",
    flows =
        @OAuthFlows(
            authorizationCode =
                @OAuthFlow(
                    authorizationUrl =
                        "https://sso.scrooge.io/realms/scrooge-realm/protocol/openid-connect/auth",
                    tokenUrl =
                        "https://sso.scrooge.io/realms/scrooge-realm/protocol/openid-connect/token",
                    refreshUrl =
                        "https://sso.scrooge.io/realms/scrooge-realm/protocol/openid-connect/token")))
public class SwaggerConfiguration {

  /*
   * Non dimenticare di settare il clientId (ed eventuale clientSecret) nelle properties :)
   * springdoc:
   *   swagger-ui:
   *     oauth:
   *       client-id: client-id-impostato-in-keycloak
   *       client-secret: client-secret-
   */
}
