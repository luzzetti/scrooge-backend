package lan.scrooge.api.application.ports.input;

import jakarta.validation.constraints.NotNull;
import lan.scrooge.api.domain.entities.ScroogeUser;
import lan.scrooge.api.domain.vos.BankAccountId;
import lan.scrooge.api.domain.vos.MnemonicName;
import lombok.Builder;

public interface CreateBankAccountUseCase {

  BankAccountId execute(CreateBankAccountCommand command);

  @Builder
  record CreateBankAccountCommand(
      @NotNull ScroogeUser currentUser, @NotNull MnemonicName mnemonicName) {}
}
