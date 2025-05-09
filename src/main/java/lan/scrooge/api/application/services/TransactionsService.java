package lan.scrooge.api.application.services;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import lan.scrooge.api._shared.QueryResultPaginated;
import lan.scrooge.api._shared.exceptions.ApplicationError;
import lan.scrooge.api._shared.exceptions.ElementNotFoundException;
import lan.scrooge.api._shared.exceptions.ElementNotValidException;
import lan.scrooge.api.application.ports.input.ListBankTransactionsQuery;
import lan.scrooge.api.application.ports.input.TransferFundsUseCase;
import lan.scrooge.api.application.ports.output.BankAccountPersistencePort;
import lan.scrooge.api.application.ports.output.BankTransactionPersistencePort;
import lan.scrooge.api.domain.entities.BankAccount;
import lan.scrooge.api.domain.entities.BankTransaction;
import lan.scrooge.api.domain.entities.ScroogeUser;
import lan.scrooge.api.domain.vos.BankAccountId;
import lan.scrooge.api.domain.vos.BankTransactionId;
import lan.scrooge.api.domain.vos.Causale;
import lan.scrooge.api.domain.vos.IBAN;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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

    // Execute various checks to prevent invalid transactions and send the corresponding error
    assertBankAccountOwnership(sourceAccount, command.currentUser());
    assertSourceAccountHasSufficientFunds(sourceAccount, command.amount());
    assertSourceAndTargetAreDifferent(command.sourceIban(), command.targetIban());

    BankTransaction theBankTransaction =
        createBankTransaction(sourceAccount, targetAccount, command.amount(), command.causale());
    bankTransactionPersistencePort.append(theBankTransaction);

    // move funds
    sourceAccount.elaborate(theBankTransaction);
    targetAccount.elaborate(theBankTransaction);

    // save both
    bankAccountPersistencePort.persist(sourceAccount);
    bankAccountPersistencePort.persist(targetAccount);

    return theBankTransaction.getId();
  }

  private void assertSourceAndTargetAreDifferent(
      @NotNull IBAN sourceIban, @NotNull IBAN targetIban) {

    if (Objects.equals(sourceIban, targetIban)) {
      throw new ElementNotValidException(Errors.NOT_VALID_TRANSFER_ON_SAME_ACCOUNT);
    }
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

  /*
   * Crea e valida una nuova transazione bancaria
   */
  private static BankTransaction createBankTransaction(
      BankAccount sourceAccount, BankAccount targetAccount, BigDecimal amount, Causale causale) {

    return BankTransaction.builder()
        .id(BankTransactionId.generate())
        .sourceAccountId(sourceAccount.getId())
        .targetAccountId(targetAccount.getId())
        .amount(amount)
        .causale(causale)
        .build();
  }

  private static void assertSourceAccountHasSufficientFunds(
      BankAccount sourceAccount, BigDecimal amount) {
    if (!sourceAccount.canWithdrawn(amount)) {
      throw new ElementNotValidException(Errors.NOT_VALID_FUNDS_INSUFFICIENT);
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
      throw new ElementNotFoundException(Errors.NOT_FOUND_BANK_ACCOUNT);
    }
  }

  private BankAccount fetchAccount(IBAN iban) {
    return bankAccountPersistencePort
        .fetchFromIban(iban)
        .orElseThrow(() -> new ElementNotFoundException(Errors.NOT_FOUND_BANK_ACCOUNT));
  }

  private BankAccount fetchAccount(BankAccountId bankAccountId) {
    return bankAccountPersistencePort.fetch(bankAccountId);
  }

  private List<BankTransaction> fetchTransactionHistory(BankAccountId bankAccountId) {
    return bankTransactionPersistencePort.fetchAll(bankAccountId);
  }

  @Getter
  @RequiredArgsConstructor
  private enum Errors implements ApplicationError {
    NOT_VALID_FUNDS_INSUFFICIENT("not-valid.funds.insufficient"),
    NOT_FOUND_BANK_ACCOUNT("not-found.bank-account"),
    NOT_VALID_TRANSFER_ON_SAME_ACCOUNT("not-valid.transfer.on.same.account");
    private final String code;
  }
}
