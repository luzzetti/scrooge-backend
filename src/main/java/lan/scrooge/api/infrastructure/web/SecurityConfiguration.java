package lan.scrooge.api.infrastructure.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

  private final SecurityProperties properties;
  private final Map<String, AuthenticationManager> authenticationManagers = new HashMap<>();

  private final ScroogePrincipalConverter scroogePrincipalConverter;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
        conf ->
            conf.requestMatchers(publicRoutes())
                .permitAll()
                .requestMatchers("/**")
                .authenticated()
                .anyRequest()
                .denyAll());

    // Carico i resolver per i tenant multipli, agganciandogli il mio converter
    properties.getTrustedIssuers().forEach(issuer -> addManager(authenticationManagers, issuer));
    var managerResolver = new JwtIssuerAuthenticationManagerResolver(authenticationManagers::get);
    http.oauth2ResourceServer(
        oauth2Conf -> oauth2Conf.authenticationManagerResolver(managerResolver));

    // Dico al WebServer che non voglio cookie
    http.sessionManagement(conf -> conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    // SENZA DI QUESTO, La corsConfigurationSource NON VIENE ELABORATA!
    http.cors(Customizer.withDefaults());

    // (In teoria) non serve
    http.csrf(AbstractHttpConfigurer::disable);

    return http.build();
  }

  private void addManager(
      Map<String, AuthenticationManager> authenticationManagers, String issuer) {
    JwtAuthenticationProvider authenticationProvider =
        new JwtAuthenticationProvider(JwtDecoders.fromOidcIssuerLocation(issuer));
    authenticationProvider.setJwtAuthenticationConverter(scroogePrincipalConverter);
    authenticationManagers.put(issuer, authenticationProvider::authenticate);
  }

  /**
   * Rotte pubbliche al di fuori della security
   *
   * @return
   */
  private String[] publicRoutes() {
    return properties.getPublicUrls().toArray(new String[0]);
  }

  /***
   * Attenzione!
   * Questo BEAN viene preso solamente se nelle configurazioni sopra, è presente:
   * http.cors()
   */
  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    configuration.setAllowedOrigins(List.of("*"));

    configuration.addAllowedHeader("*");

    configuration.setAllowedMethods(
        List.of(
            HttpMethod.GET.name(),
            HttpMethod.POST.name(),
            HttpMethod.OPTIONS.name(),
            HttpMethod.HEAD.name(),
            HttpMethod.PUT.name(),
            HttpMethod.PATCH.name(),
            HttpMethod.DELETE.name()));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  /* Extra:
   * Mapping avanzato per ruoli https://www.baeldung.com/spring-security-map-authorities-jwt
   */

}
