package lan.scrooge.api.infrastructure.events.publishers;

import lan.scrooge.api.application.ports.output.BankAccountMessagingPort;
import lan.scrooge.api.domain.entities.BankAccount;
import lan.scrooge.api.infrastructure.events.models.BankAccountCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BankAccountPublisherAdapter implements BankAccountMessagingPort {

  private final ApplicationEventPublisher applicationEventPublisher;

  @Override
  public void sendBankAccountCreatedEvent(BankAccount bankAccount) {

    var event = new BankAccountCreatedEvent(bankAccount.getId(), bankAccount.getIban());

    applicationEventPublisher.publishEvent(event);
  }
}
