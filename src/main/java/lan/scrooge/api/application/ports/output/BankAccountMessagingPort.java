package lan.scrooge.api.application.ports.output;

import lan.scrooge.api.domain.entities.BankAccount;

public interface BankAccountMessagingPort {

  void sendBankAccountCreatedEvent(BankAccount bankAccount);
}
