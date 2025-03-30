package lan.scrooge.api.application.ports.input;

import jakarta.validation.constraints.NotNull;
import lan.scrooge.api.domain.entities.BankAccount;
import lan.scrooge.api.domain.entities.ScroogeUser;
import lan.scrooge.api.domain.vos.BankAccountId;
import lombok.Builder;

public interface ShowBankAccountQuery {

  BankAccount showBankAccount(ShowBankAccountCriterion command);

  @Builder
  record ShowBankAccountCriterion(
      @NotNull ScroogeUser currentUser, @NotNull BankAccountId bankAccountId) {}
}
