package lan.scrooge.api.application.ports.output;

import jakarta.validation.constraints.NotNull;
import lan.scrooge.api.domain.entities.BankAccount;
import lan.scrooge.api.domain.entities.ScroogeUser;
import lan.scrooge.api.domain.vos.BankAccountId;

import java.util.List;

public interface BankAccountPersistencePort {

  void persist(BankAccount bankAccount);

  BankAccount fetch(BankAccountId bankAccountId);

  List<BankAccount> fetchAll(@NotNull ScroogeUser scroogeUser);
}
