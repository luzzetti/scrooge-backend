package lan.scrooge.api.infrastructure.jpa;

import java.util.List;
import java.util.UUID;
import lan.scrooge.api._shared.QueryResultPaginated;
import lan.scrooge.api.application.ports.input.ListUserTransactionsQuery;
import lan.scrooge.api.application.ports.output.BankTransactionPersistencePort;
import lan.scrooge.api.domain.entities.BankTransaction;
import lan.scrooge.api.domain.vos.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class BankTransactionJpaAdapter
    implements BankTransactionPersistencePort, ListUserTransactionsQuery {

  private final BankTransactionRepository bankTransactionRepository;
  private final BankAccountRepository bankAccountRepository;

  @Override
  public void append(BankTransaction theBankTransaction) {

    var sourceAccountProxied =
        bankAccountRepository.getReferenceById(theBankTransaction.getSourceAccountId().getValue());

    var targetAccountProxied =
        bankAccountRepository.getReferenceById(theBankTransaction.getTargetAccountId().getValue());

    var entity = new BankTransactionJpaEntity();
    entity.setId(theBankTransaction.getId().getValue());
    entity.setSourceAccount(sourceAccountProxied);
    entity.setTargetAccount(targetAccountProxied);
    entity.setAmount(theBankTransaction.getAmount());
    entity.setCausale(theBankTransaction.getCausale() != null ? theBankTransaction.getCausale().getValue() : null);
    entity.setCreatedAt(theBankTransaction.getCreatedAt());

    bankTransactionRepository.save(entity);
  }

  @Override
  public List<BankTransaction> fetchAll(BankAccountId bankAccountId) {
    return bankTransactionRepository.findAllByBankAccountId(bankAccountId.getValue()).stream()
        .map(BankTransactionJpaAdapter::toBankTransaction)
        .toList();
  }

  private static BankTransaction toBankTransaction(BankTransactionJpaEntity bt) {
    return BankTransaction.builder()
        .id(BankTransactionId.of(bt.getId()))
        .sourceAccountId(BankAccountId.of(bt.getSourceAccount().getId()))
        .targetAccountId(BankAccountId.of(bt.getTargetAccount().getId()))
        .amount(bt.getAmount())
        .causale(bt.getCausale() != null ? Causale.of(bt.getCausale()) : null)
        .createdAt(bt.getCreatedAt())
        .build();
  }

  @Override
  public QueryResultPaginated<UserTransactionProjection> listUserTransactions(
      ListUserTransactionsCriterion command) {

    UUID userId = command.currentUser().getId().getValue();
    List<UserTransactionProjection> userTransactions =
        bankTransactionRepository.findAllByUserId(userId, command.pageNumber(), command.pageSize());

    return QueryResultPaginated.<UserTransactionProjection>builder()
        .results(userTransactions)
        .totalElements(userTransactions.size())
        .pageNumber(0)
        .pageSize(Integer.MAX_VALUE)
        .build();
  }

}
