package lan.scrooge.api.infrastructure.web.fund_transfer;

import lan.scrooge.api.application.ports.input.TransferFundsUseCase;
import lan.scrooge.api.domain.entities.ScroogeUser;
import lan.scrooge.api.domain.vos.IBAN;
import lan.scrooge.api.domain.vos.TransactionId;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("1.0/transactions")
@RequiredArgsConstructor
@Log4j2
public class FundTransferRestController implements FundTransferRestApi {

  private final TransferFundsUseCase transferFundsUseCase;

  @PostMapping
  @Override
  public ResponseEntity<FundsTransferResponse> transferFunds(
      @AuthenticationPrincipal ScroogeUser principal, @RequestBody FundsTransferRequest request) {

    var command =
        TransferFundsUseCase.TransferFundsCommand.builder()
            .currentUser(principal)
            .sourceIban(new IBAN(request.getSourceIban()))
            .targetIban(new IBAN(request.getTargetIban()))
            .amount(request.getAmount())
            .build();

    TransactionId transactionId = transferFundsUseCase.transfer(command);

    var response = new FundsTransferResponse(transactionId.asText());
    return ResponseEntity.ok(response);
  }
}
