package lan.scrooge.api.infrastructure.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lan.scrooge.api.application.ports.output.BankAccountPersistencePort;
import lan.scrooge.api.domain.entities.BankAccount;
import lan.scrooge.api.domain.entities.ScroogeUser;
import lan.scrooge.api.domain.vos.BankAccountId;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class BankAccountPersistenceMockAdapter implements BankAccountPersistencePort {

  private Map<BankAccountId, BankAccount> database = new HashMap<>();

  @Override
  public void persist(BankAccount bankAccount) {
    database.put(bankAccount.getId(), bankAccount);
  }

  @Override
  public BankAccount fetch(BankAccountId bankAccountId) {
    BankAccount bankAccount = database.get(bankAccountId);
    if (bankAccount == null) {
      throw new IllegalArgumentException("Bank account not found");
    }
    return bankAccount;
  }

  @Override
  public List<BankAccount> fetchAll(ScroogeUser scroogeUser) {
    return database.values()
            .stream()
            .filter(ba -> ba.getOwner().getId().equals(scroogeUser.getId()))
            .toList();
  }
}
