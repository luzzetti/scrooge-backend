package lan.scrooge.api.application.ports.input;

import jakarta.validation.constraints.NotNull;
import lan.scrooge.api.domain.entities.ScroogeUser;
import lan.scrooge.api.domain.vos.BankAccountId;
import lan.scrooge.api.domain.vos.IBAN;
import lan.scrooge.api.domain.vos.RemainingFundDestination;
import lombok.Builder;

public interface CloseBankAccountUseCase {

  void close(CloseBankAccountCommand command);

  @Builder
  record CloseBankAccountCommand(
      @NotNull ScroogeUser currentUser,
      @NotNull BankAccountId bankAccountId,
      @NotNull RemainingFundDestination destination,
      IBAN ibanDestination) {}
}
