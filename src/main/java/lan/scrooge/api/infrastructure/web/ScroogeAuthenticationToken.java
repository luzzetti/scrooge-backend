package lan.scrooge.api.infrastructure.web;

import java.util.Collections;
import java.util.List;

import lan.scrooge.api.domain.entities.ScroogeUser;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

@Log4j2
@EqualsAndHashCode(callSuper = false)
public class ScroogeAuthenticationToken extends AbstractAuthenticationToken {

  private final ScroogeUser principal;
  private final Jwt credentials;

  public ScroogeAuthenticationToken(Jwt jwt, ScroogeUser principal) {
    super(getAuthorities(jwt));
    this.setAuthenticated(true);
    this.credentials = jwt;
    this.principal = principal;
  }

  private static List<SimpleGrantedAuthority> getAuthorities(Jwt source) {

    return Collections.emptyList();
    //    final Map<String, List<String>> realmAccess =
    //        Objects.requireNonNullElse(
    //            (Map<String, List<String>>) source.getClaims().get("realm_access"),
    //            Collections.emptyMap());
    //
    //    return realmAccess.getOrDefault("roles", Collections.emptyList()).stream()
    //        .map(roleName -> "ROLE_" + roleName) // prefix to map to a Spring Security "role"
    //        .map(SimpleGrantedAuthority::new)
    //        .toList();
  }

  @Override
  public Object getCredentials() {
    return this.credentials;
  }

  @Override
  public Object getPrincipal() {
    return this.principal;
  }
}
