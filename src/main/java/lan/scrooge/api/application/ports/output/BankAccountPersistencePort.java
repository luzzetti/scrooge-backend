package lan.scrooge.api.application.ports.output;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import lan.scrooge.api.domain.entities.BankAccount;
import lan.scrooge.api.domain.entities.ScroogeUser;
import lan.scrooge.api.domain.vos.BankAccountId;
import lan.scrooge.api.domain.vos.IBAN;

public interface BankAccountPersistencePort {

  void persist(BankAccount bankAccount);

  BankAccount fetch(BankAccountId bankAccountId);

  List<BankAccount> fetchAll(@NotNull ScroogeUser scroogeUser);

  Optional<BankAccount> fetchFromIban(@NotNull IBAN iban);

}
