package lan.scrooge.api.application.ports.input;

import jakarta.validation.constraints.NotNull;
import lan.scrooge.api._shared.QueryResultPaginated;
import lan.scrooge.api.domain.entities.BankTransaction;
import lan.scrooge.api.domain.entities.ScroogeUser;
import lan.scrooge.api.domain.vos.BankAccountId;
import lombok.Builder;

public interface ListBankTransactionsQuery {

  QueryResultPaginated<BankTransaction> listBankTransactions(ListBankTransactionsCriterion command);

  @Builder
  record ListBankTransactionsCriterion(
      @NotNull ScroogeUser currentUser, @NotNull BankAccountId bankAccountId) {}
}
