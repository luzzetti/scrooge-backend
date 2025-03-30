package lan.scrooge.api.application.services;

import lan.scrooge.api.application.ports.input.TransferFundsUseCase;
import lan.scrooge.api.application.ports.output.BankAccountPersistencePort;
import lan.scrooge.api.domain.entities.BankAccount;
import lan.scrooge.api.domain.vos.TransactionId;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
// Transactional
@RequiredArgsConstructor
@Log4j2
public class TransactionsService implements TransferFundsUseCase {

  private final BankAccountPersistencePort bankAccountPersistencePort;

  @Override
  public TransactionId transfer(TransferFundsCommand command) {

    TransactionId transactionId = TransactionId.generate();

    // Get bankAccounts from iban
    BankAccount sourceAccount =
        bankAccountPersistencePort.fetchFromIban(command.sourceIban()).orElseThrow();
    BankAccount targetAccount =
        bankAccountPersistencePort.fetchFromIban(command.targetIban()).orElseThrow();
    // Check if currentUser is the owner of the source

    // Il vero errore è che l'utente corrente non è il proprietario del conto
    // e di conseguenza non può leggerlo
    // ma per convenzione ritorniamo che il conto non esiste
    // per evitare di mostrare informazioni ad eventuali malintenzionati
    if (!sourceAccount.hasOwner(command.currentUser())) {
      throw new IllegalArgumentException("Bank account not found");
    }

    // Check if bank account has the money
    if (!sourceAccount.canWithdrawn(command.amount())) {
      throw new IllegalArgumentException("Not enough money");
    }

    // move money
    sourceAccount.withdrawn(command.amount());
    targetAccount.deposit(command.amount());

    // save both
    bankAccountPersistencePort.persist(sourceAccount);
    bankAccountPersistencePort.persist(targetAccount);

    return transactionId;
  }
}
