package lan.scrooge.api.infrastructure.web;

import lan.scrooge.api.domain.entities.ScroogeUser;
import lan.scrooge.api.domain.vos.Email;
import lan.scrooge.api.domain.vos.ScroogeUserId;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;

@Log4j2
public class ScroogePrincipalConverter implements Converter<Jwt, ScroogeAuthenticationToken> {

  @Override
  public ScroogeAuthenticationToken convert(Jwt source) {

    // These are the info that comes from the jwt given by Keycloak
    ScroogeUserId thePrincipalId = ScroogeUserId.of(source.getClaimAsString("sub"));
    Email thePrincipalEmail = Email.of(source.getClaimAsString("email"));

    ScroogeUser user = ScroogeUser.builder().id(thePrincipalId).email(thePrincipalEmail).build();

    log.debug(() -> "Correctly converted JWT token to UserPrincipal %s".formatted(user));
    return new ScroogeAuthenticationToken(source, user);
  }
}
