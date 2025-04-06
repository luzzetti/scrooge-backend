package lan.scrooge.api.infrastructure.jpa;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lan.scrooge.api._shared.exceptions.ApplicationError;
import lan.scrooge.api._shared.exceptions.ElementNotFoundException;
import lan.scrooge.api.application.ports.output.BankAccountPersistencePort;
import lan.scrooge.api.domain.entities.BankAccount;
import lan.scrooge.api.domain.entities.ScroogeUser;
import lan.scrooge.api.domain.vos.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class BankAccountPersistenceMockAdapter implements BankAccountPersistencePort {

  private Map<BankAccountId, BankAccount> database = new HashMap<>();

  @PostConstruct
  public void init() {
    // Prepopola il DB con un conto di default
    log.error("Adding a dummy bank account");
    BankAccountId bankAccountId = BankAccountId.generate();
    var dummyBankAccount =
        BankAccount.builder()
            .id(bankAccountId)
            .owner(
                ScroogeUser.builder()
                    .id(ScroogeUserId.generate())
                    .email(new Email("test@test.test"))
                    .build())
            .iban(new IBAN("IT60X0542811101000000123456"))
            .mnemonicName(new MnemonicName("rich"))
            .balance(BigDecimal.valueOf(10000))
            .build();

    this.database.put(bankAccountId, dummyBankAccount);
  }

  @Override
  public void persist(BankAccount bankAccount) {
    database.put(bankAccount.getId(), bankAccount);
  }

  @Override
  public BankAccount fetch(BankAccountId bankAccountId) {
    BankAccount bankAccount = database.get(bankAccountId);
    if (bankAccount == null) {
      throw new ElementNotFoundException(Errors.NOT_FOUND_BANK_ACCOUNT);
    }
    return bankAccount;
  }

  @Override
  public List<BankAccount> fetchAll(ScroogeUser scroogeUser) {
    return database.values().stream().filter(ba -> ba.hasOwner(scroogeUser)).toList();
  }

  @Override
  public Optional<BankAccount> fetchFromIban(IBAN iban) {
    return database.values().stream().filter(ba -> ba.getIban().equals(iban)).findFirst();
  }

  @Override
  public void delete(BankAccount theBankAccountToClose) {
    database.remove(theBankAccountToClose.getId());
  }

  @Getter
  @RequiredArgsConstructor
  private enum Errors implements ApplicationError {
    NOT_FOUND_BANK_ACCOUNT("not-found.bank-account");
    private final String code;
  }
}
