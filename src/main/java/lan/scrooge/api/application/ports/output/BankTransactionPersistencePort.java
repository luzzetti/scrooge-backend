package lan.scrooge.api.application.ports.output;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lan.scrooge.api.domain.entities.BankTransaction;
import lan.scrooge.api.domain.vos.BankAccountId;

public interface BankTransactionPersistencePort {

  void append(BankTransaction theBankTransaction);

  List<BankTransaction> fetchAll(@NotNull BankAccountId bankAccountId);
}
