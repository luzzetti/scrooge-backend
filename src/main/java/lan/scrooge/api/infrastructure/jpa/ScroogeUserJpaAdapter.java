package lan.scrooge.api.infrastructure.jpa;

import lan.scrooge.api._shared.exceptions.ApplicationError;
import lan.scrooge.api.application.ports.output.ScroogeUserPersistencePort;
import lan.scrooge.api.domain.entities.ScroogeUser;
import lan.scrooge.api.domain.vos.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class ScroogeUserJpaAdapter implements ScroogeUserPersistencePort {

  private final ScroogeUserRepository scroogeUserRepository;

  @Override
  public void upsert(ScroogeUser scroogeUser) {
    var entity = new ScroogeUserJpaEntity();
    entity.setId(scroogeUser.getId().getValue());
    entity.setEmail(scroogeUser.getEmail().getValue());

    scroogeUserRepository.save(entity);
  }



  @Getter
  @RequiredArgsConstructor
  private enum Errors implements ApplicationError {
    NOT_FOUND_BANK_ACCOUNT("not-found.bank-account");
    private final String code;
  }
}
