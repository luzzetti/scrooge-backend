package lan.scrooge.api.application.services;

import java.math.BigDecimal;
import java.util.List;
import lan.scrooge.api._shared.QueryResultPaginated;
import lan.scrooge.api._shared.exceptions.ApplicationError;
import lan.scrooge.api._shared.exceptions.ElementNotFoundException;
import lan.scrooge.api._shared.exceptions.ElementNotValidException;
import lan.scrooge.api.application.ports.input.CloseBankAccountUseCase;
import lan.scrooge.api.application.ports.input.CreateBankAccountUseCase;
import lan.scrooge.api.application.ports.input.ListBankAccountQuery;
import lan.scrooge.api.application.ports.input.ShowBankAccountQuery;
import lan.scrooge.api.application.ports.output.BankAccountPersistencePort;
import lan.scrooge.api.application.ports.output.BankTransactionPersistencePort;
import lan.scrooge.api.domain.entities.BankAccount;
import lan.scrooge.api.domain.entities.BankTransaction;
import lan.scrooge.api.domain.entities.ScroogeUser;
import lan.scrooge.api.domain.services.IbanGenerator;
import lan.scrooge.api.domain.vos.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
// Transactional
@RequiredArgsConstructor
@Log4j2
public class BankAccountService
    implements ShowBankAccountQuery,
        ListBankAccountQuery,
        CreateBankAccountUseCase,
        CloseBankAccountUseCase {

  // Todo: una volta eliminato il mock, creare un servizio per caricare le organizzazioni di
  // beneficienza
  public static final String IBAN_OF_CHARITY_ORG = "IT60X0542811101000000123456";
  private final BankAccountPersistencePort bankAccountPersistencePort;
  private final BankTransactionPersistencePort bankTransactionPersistencePort;

  @Override
  public BankAccountId create(CreateBankAccountCommand command) {

    var aBankAccount = createBankAccount(command.currentUser(), command.mnemonicName());

    persist(aBankAccount);

    return aBankAccount.getId();
  }

  private static BankAccount createBankAccount(ScroogeUser owner, MnemonicName mnemonicName) {
    return BankAccount.builder()
        .id(BankAccountId.generate())
        .mnemonicName(mnemonicName)
        .iban(IbanGenerator.generateRandomIBAN())
        .owner(owner)
        .balance(BigDecimal.valueOf(100))
        .build();
  }

  private void persist(BankAccount aBankAccount) {
    bankAccountPersistencePort.persist(aBankAccount);
  }

  @Override
  public QueryResultPaginated<BankAccount> listBankAccount(ListBankAccountCriterion command) {

    List<BankAccount> bankAccounts = bankAccountPersistencePort.fetchAll(command.currentUser());

    return QueryResultPaginated.<BankAccount>builder()
        .results(bankAccounts)
        .pageNumber(0)
        .pageSize(bankAccounts.size())
        .totalElements(bankAccounts.size())
        .build();
  }

  @Override
  public BankAccount showBankAccount(ShowBankAccountCriterion command) {

    BankAccount theBankAccount = fetchBankAccount(command.bankAccountId());

    // Il vero errore è che l'utente corrente non è il proprietario del conto
    // e di conseguenza non può leggerlo
    // ma per convenzione ritorniamo che il conto non esiste
    // per evitare di mostrare informazioni ad eventuali malintenzionati
    if (!theBankAccount.hasOwner(command.currentUser())) {
      throw new ElementNotFoundException(Errors.NOT_FOUND_BANK_ACCOUNT);
    }

    return theBankAccount;
  }

  @Override
  public void close(CloseBankAccountCommand command) {

    var theBankAccountToClose = fetchBankAccount(command.bankAccountId());
    assertBankAccountOwnership(theBankAccountToClose, command.currentUser());

    // Se non ha fondi rimanenti chiudi account e ritorna
    if (!theBankAccountToClose.hasFunds()) {
      bankAccountPersistencePort.delete(theBankAccountToClose);
      return;
    }

    BankAccount beneficiaryOfRemainingFunds =
        getBeneficiaryBankAccount(command.destination(), command.ibanDestination());

    var remainingFunds = theBankAccountToClose.getBalance();
    BankTransaction theBankTransaction =
        createBankTransaction(theBankAccountToClose, beneficiaryOfRemainingFunds, remainingFunds);
    bankTransactionPersistencePort.append(theBankTransaction);

    // move remaining funds
    theBankAccountToClose.elaborate(theBankTransaction);
    beneficiaryOfRemainingFunds.elaborate(theBankTransaction);

    // save both
    bankAccountPersistencePort.delete(theBankAccountToClose);
    bankAccountPersistencePort.persist(beneficiaryOfRemainingFunds);
  }

  private BankAccount getBeneficiaryBankAccount(RemainingFundDestination destination, IBAN iban) {

    if (destination == RemainingFundDestination.IBAN) {
      return fetchBankAccount(iban);

    } else if (destination == RemainingFundDestination.CHARITY) {
      return fetchBankAccount(new IBAN(IBAN_OF_CHARITY_ORG));

    } else {
      throw new ElementNotValidException(Errors.NOT_VALID_FUND_DESTINATION_OPTION);
    }
  }

  private static void assertBankAccountOwnership(BankAccount sourceAccount, ScroogeUser user) {
    if (!sourceAccount.hasOwner(user)) {
      throw new ElementNotFoundException(Errors.NOT_FOUND_BANK_ACCOUNT);
    }
  }

  private BankAccount fetchBankAccount(BankAccountId bankAccountId) {
    return bankAccountPersistencePort.fetch(bankAccountId);
  }

  private BankAccount fetchBankAccount(IBAN iban) {
    return bankAccountPersistencePort
        .fetchFromIban(iban)
        .orElseThrow(() -> new ElementNotFoundException(Errors.NOT_FOUND_DESTINATION_BANK_ACCOUNT));
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

  @Getter
  @RequiredArgsConstructor
  private enum Errors implements ApplicationError {
    NOT_FOUND_BANK_ACCOUNT("not-found.bank-account"),
    NOT_FOUND_DESTINATION_BANK_ACCOUNT("not-found.destination-bank-account"),
    NOT_VALID_FUND_DESTINATION_OPTION("not-valid.fund.destination.option");
    private final String code;
  }
}
