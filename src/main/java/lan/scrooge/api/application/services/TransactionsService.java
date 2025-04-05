package lan.scrooge.api.application.services;

import java.math.BigDecimal;
import java.util.List;
import lan.scrooge.api._shared.QueryResultPaginated;
import lan.scrooge.api.application.ports.input.ListBankTransactionsQuery;
import lan.scrooge.api.application.ports.input.TransferFundsUseCase;
import lan.scrooge.api.application.ports.output.BankAccountPersistencePort;
import lan.scrooge.api.application.ports.output.BankTransactionPersistencePort;
import lan.scrooge.api.domain.entities.BankAccount;
import lan.scrooge.api.domain.entities.BankTransaction;
import lan.scrooge.api.domain.entities.ScroogeUser;
import lan.scrooge.api.domain.vos.BankAccountId;
import lan.scrooge.api.domain.vos.BankTransactionId;
import lan.scrooge.api.domain.vos.IBAN;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
// Transactional
@RequiredArgsConstructor
@Log4j2
public class TransactionsService implements TransferFundsUseCase, ListBankTransactionsQuery {

  private final BankAccountPersistencePort bankAccountPersistencePort;
  private final BankTransactionPersistencePort bankTransactionPersistencePort;

  @Override
  public BankTransactionId transfer(TransferFundsCommand command) {

    // Get bankAccounts from iban
    BankAccount sourceAccount = fetchAccount(command.sourceIban());
    BankAccount targetAccount = fetchAccount(command.targetIban());

    // Check if currentUser is the owner of the source
    assertBankAccountOwnership(sourceAccount, command.currentUser());
    // Check if bank account has the money
    assertSourceAccountHasEnoughFunds(sourceAccount, command.amount());

    BankTransaction theBankTransaction =
        createBankTransaction(sourceAccount, targetAccount, command.amount());
    bankTransactionPersistencePort.append(theBankTransaction);

    // move funds
    sourceAccount.elaborate(theBankTransaction);
    targetAccount.elaborate(theBankTransaction);

    // save both
    bankAccountPersistencePort.persist(sourceAccount);
    bankAccountPersistencePort.persist(targetAccount);

    return theBankTransaction.getId();
  }

  /*
   * Crea e valida una nuova transazione bancaria
   */
  private static BankTransaction createBankTransaction(
      BankAccount sourceAccount, BankAccount targetAccount, BigDecimal amount) {

    return BankTransaction.builder()
        .id(BankTransactionId.generate())
        .sourceAccountId(sourceAccount.getId())
        .targetAccountId(targetAccount.getId())
        .amount(amount)
        .build();
  }

  private static void assertSourceAccountHasEnoughFunds(
      BankAccount sourceAccount, BigDecimal amount) {
    if (!sourceAccount.canWithdrawn(amount)) {
      throw new IllegalArgumentException("Not enough funds on your account");
    }
  }

  /*
   * Il vero errore è che l'utente corrente non è il proprietario del conto
   * e di conseguenza non può leggerlo
   * ma per convenzione ritorniamo che il conto non esiste
   * per evitare di mostrare informazioni ad eventuali malintenzionati
   */
  private static void assertBankAccountOwnership(BankAccount sourceAccount, ScroogeUser user) {
    if (!sourceAccount.hasOwner(user)) {
      throw new IllegalArgumentException("Bank account not found");
    }
  }

  private BankAccount fetchAccount(IBAN iban) {
    return bankAccountPersistencePort.fetchFromIban(iban).orElseThrow();
  }

  private BankAccount fetchAccount(BankAccountId bankAccountId) {
    return bankAccountPersistencePort.fetch(bankAccountId);
  }

  // Read-only transaction
  @Override
  public QueryResultPaginated<BankTransaction> listBankTransactions(
      ListBankTransactionsCriterion command) {

    BankAccount theAccount = fetchAccount(command.bankAccountId());
    assertBankAccountOwnership(theAccount, command.currentUser());

    var transactionList = fetchTransactionHistory(command.bankAccountId());

    return QueryResultPaginated.<BankTransaction>builder()
        .results(transactionList)
        .pageNumber(0)
        .pageSize(Integer.MAX_VALUE)
        .totalElements(transactionList.size())
        .build();
  }

  private List<BankTransaction> fetchTransactionHistory(BankAccountId bankAccountId) {
    return bankTransactionPersistencePort.fetchAll(bankAccountId);
  }
}
