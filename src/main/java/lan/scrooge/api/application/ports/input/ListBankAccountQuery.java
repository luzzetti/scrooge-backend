package lan.scrooge.api.application.ports.input;

import jakarta.validation.constraints.NotNull;
import lan.scrooge.api._shared.QueryResultPaginated;
import lan.scrooge.api.domain.entities.BankAccount;
import lan.scrooge.api.domain.entities.ScroogeUser;
import lombok.Builder;

public interface ListBankAccountQuery {

  QueryResultPaginated<BankAccount> listBankAccount(ListBankAccountCriterion command);

  @Builder
  record ListBankAccountCriterion(@NotNull ScroogeUser currentUser) {}
}
