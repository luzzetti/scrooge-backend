package lan.scrooge.api.application.ports.input;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lan.scrooge.api.domain.entities.ScroogeUser;
import lan.scrooge.api.domain.vos.IBAN;
import lan.scrooge.api.domain.vos.BankTransactionId;
import lan.scrooge.api.domain.vos.Causale;
import lombok.Builder;

public interface TransferFundsUseCase {

  BankTransactionId transfer(TransferFundsCommand command);

  @Builder
  record TransferFundsCommand(
      @NotNull ScroogeUser currentUser,
      @NotNull IBAN sourceIban,
      @NotNull IBAN targetIban,
      @NotNull BigDecimal amount,
      @NotNull Causale causale) {}
}
