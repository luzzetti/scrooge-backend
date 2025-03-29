package lan.scrooge.api.application.services;

import java.util.List;
import lan.scrooge.api._shared.QueryResultPaginated;
import lan.scrooge.api.application.ports.input.CreateBankAccountUseCase;
import lan.scrooge.api.application.ports.input.ListBankAccountQuery;
import lan.scrooge.api.application.ports.output.BankAccountPersistencePort;
import lan.scrooge.api.domain.entities.BankAccount;
import lan.scrooge.api.domain.services.IbanGenerator;
import lan.scrooge.api.domain.vos.BankAccountId;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
// Transactional
@RequiredArgsConstructor
@Log4j2
public class BankAccountService implements ListBankAccountQuery, CreateBankAccountUseCase {

  private final BankAccountPersistencePort bankAccountPersistencePort;

  @Override
  public BankAccountId execute(CreateBankAccountCommand command) {

    // Create a new BankAccount

    var anIban = IbanGenerator.generateRandomIBAN();

    BankAccountId bankAccountId = BankAccountId.generate();
    var aBankAccount =
        BankAccount.builder()
            .id(bankAccountId)
            .mnemonicName(command.mnemonicName())
            .iban(anIban)
            .owner(command.currentUser())
            .build();

    // Persist
    bankAccountPersistencePort.persist(aBankAccount);

    return bankAccountId;
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
}
