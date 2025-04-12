package lan.scrooge.api.application.services;

import lan.scrooge.api._shared.exceptions.ApplicationError;
import lan.scrooge.api.application.ports.input.*;
import lan.scrooge.api.application.ports.output.ScroogeUserPersistencePort;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class ScroogeUserService implements InitializeUserUseCase {

  private final ScroogeUserPersistencePort scroogeUserPersistencePort;

  @Override
  public void initializeUser(InitializeUserCommand command) {
    scroogeUserPersistencePort.upsert(command.currentUser());
  }

  @Getter
  @RequiredArgsConstructor
  private enum Errors implements ApplicationError {
    NOT_VALID_FUND_DESTINATION_OPTION("not-valid.fund.destination.option");
    private final String code;
  }
}
