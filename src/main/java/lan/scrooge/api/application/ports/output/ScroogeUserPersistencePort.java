package lan.scrooge.api.application.ports.output;

import jakarta.validation.constraints.NotNull;
import lan.scrooge.api.domain.entities.ScroogeUser;

public interface ScroogeUserPersistencePort {

  void upsert(@NotNull ScroogeUser scroogeUser);
}
