package lan.scrooge.api.infrastructure.web;

import lan.scrooge.api.application.ports.input.InitializeUserUseCase;
import lan.scrooge.api.domain.entities.ScroogeUser;
import lan.scrooge.api.domain.vos.Email;
import lan.scrooge.api.domain.vos.ScroogeUserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class ScroogePrincipalConverter implements Converter<Jwt, ScroogeAuthenticationToken> {

  private final InitializeUserUseCase initializeUserUseCase;

  @Override
  public ScroogeAuthenticationToken convert(Jwt source) {

    // These are the info that comes from the jwt given by Keycloak
    ScroogeUserId thePrincipalId = ScroogeUserId.of(source.getClaimAsString("sub"));
    Email thePrincipalEmail = Email.of(source.getClaimAsString("email"));

    ScroogeUser user = ScroogeUser.builder().id(thePrincipalId).email(thePrincipalEmail).build();

    var command = InitializeUserUseCase.InitializeUserCommand.builder().currentUser(user).build();
    initializeUserUseCase.initializeUser(command);

    log.debug(() -> "Correctly converted JWT token to UserPrincipal %s".formatted(user));
    return new ScroogeAuthenticationToken(source, user);
  }
}
