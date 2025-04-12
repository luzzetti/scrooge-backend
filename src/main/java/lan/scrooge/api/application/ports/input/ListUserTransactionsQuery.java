package lan.scrooge.api.application.ports.input;

import jakarta.validation.constraints.NotNull;
import lan.scrooge.api._shared.QueryResultPaginated;
import lan.scrooge.api.domain.entities.ScroogeUser;
import lan.scrooge.api.infrastructure.jpa.UserTransactionProjection;
import lombok.Builder;

public interface ListUserTransactionsQuery {

  QueryResultPaginated<UserTransactionProjection> listUserTransactions(
      ListUserTransactionsCriterion command);

  @Builder
  record ListUserTransactionsCriterion(
      @NotNull ScroogeUser currentUser, Integer pageNumber, Integer pageSize) {}
}
