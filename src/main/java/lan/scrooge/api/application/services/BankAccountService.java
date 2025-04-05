package lan.scrooge.api.application.services;

import java.math.BigDecimal;
import java.util.List;
import lan.scrooge.api._shared.QueryResultPaginated;
import lan.scrooge.api.application.ports.input.CreateBankAccountUseCase;
import lan.scrooge.api.application.ports.input.ListBankAccountQuery;
import lan.scrooge.api.application.ports.input.ShowBankAccountQuery;
import lan.scrooge.api.application.ports.output.BankAccountPersistencePort;
import lan.scrooge.api.domain.entities.BankAccount;
import lan.scrooge.api.domain.entities.ScroogeUser;
import lan.scrooge.api.domain.services.IbanGenerator;
import lan.scrooge.api.domain.vos.BankAccountId;
import lan.scrooge.api.domain.vos.MnemonicName;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
// Transactional
@RequiredArgsConstructor
@Log4j2
public class BankAccountService
    implements ShowBankAccountQuery, ListBankAccountQuery, CreateBankAccountUseCase {

  private final BankAccountPersistencePort bankAccountPersistencePort;

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

    BankAccount theBankAccount = bankAccountPersistencePort.fetch(command.bankAccountId());

    // Il vero errore è che l'utente corrente non è il proprietario del conto
    // e di conseguenza non può leggerlo
    // ma per convenzione ritorniamo che il conto non esiste
    // per evitare di mostrare informazioni ad eventuali malintenzionati
    if (!theBankAccount.hasOwner(command.currentUser())) {
      throw new IllegalArgumentException("Bank account not found");
    }

    return theBankAccount;
  }
}
