package lan.scrooge.api.infrastructure.jpa;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lan.scrooge.api.application.ports.output.BankTransactionPersistencePort;
import lan.scrooge.api.domain.entities.BankTransaction;
import lan.scrooge.api.domain.vos.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class TransactionPersistenceMockAdapter implements BankTransactionPersistencePort {

  private Map<BankTransactionId, BankTransaction> database = new HashMap<>();

  @PostConstruct
  public void init() {
    // Prepopola il DB con un conto di default
  }

  @Override
  public void append(BankTransaction theBankTransaction) {
    database.put(theBankTransaction.getId(), theBankTransaction);
  }

  @Override
  public List<BankTransaction> fetchAll(BankAccountId bankAccountId) {

    return database.values().stream()
        .filter(
            bt ->
                bt.getSourceAccountId().equals(bankAccountId)
                    || bt.getTargetAccountId().equals(bankAccountId))
        .toList();
  }
}
