package lan.scrooge.api.infrastructure.events.listeners;

import java.math.BigDecimal;
import lan.scrooge.api.application.ports.input.TransferFundsUseCase;
import lan.scrooge.api.domain.entities.ScroogeUser;
import lan.scrooge.api.domain.vos.Causale;
import lan.scrooge.api.domain.vos.Email;
import lan.scrooge.api.domain.vos.IBAN;
import lan.scrooge.api.domain.vos.ScroogeUserId;
import lan.scrooge.api.infrastructure.events.models.BankAccountCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class BankAccountListenerAdapter {

  private final TransferFundsUseCase transferFundsUseCase;

  @EventListener
  public void onBankAccountCreated_initAccountWithCharityMoney(BankAccountCreatedEvent event) {

    var command =
        TransferFundsUseCase.TransferFundsCommand.builder()
            .currentUser(getCharityPrincipal())
            .sourceIban(IBAN.of("IT60X0542811101000000123456"))
            .targetIban(event.iban())
            .amount(BigDecimal.valueOf(1000))
            .causale(
                Causale.of("Ti è stata accreditata una somma iniziale da un'ente di beneficienza"))
            .build();

    transferFundsUseCase.transfer(command);
  }

  private static ScroogeUser getCharityPrincipal() {
    return ScroogeUser.builder()
        .id(ScroogeUserId.of("b264b173-a4eb-445c-a33e-40e17b2f8e8d"))
        .email(Email.of("charity@charity.charity"))
        .build();
  }
}
