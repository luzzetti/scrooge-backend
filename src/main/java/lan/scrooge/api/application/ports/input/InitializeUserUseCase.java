package lan.scrooge.api.application.ports.input;

import jakarta.validation.constraints.NotNull;
import lan.scrooge.api.domain.entities.ScroogeUser;
import lombok.Builder;

public interface InitializeUserUseCase {

  void initializeUser(InitializeUserCommand command);

  @Builder
  record InitializeUserCommand(@NotNull ScroogeUser currentUser) {}
}
