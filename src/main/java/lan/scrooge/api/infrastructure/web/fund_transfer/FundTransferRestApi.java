package lan.scrooge.api.infrastructure.web.fund_transfer;

import lan.scrooge.api.domain.entities.ScroogeUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface FundTransferRestApi {

  @PostMapping
  ResponseEntity<FundsTransferResponse> transferFunds(
      @AuthenticationPrincipal ScroogeUser principal, @RequestBody FundsTransferRequest request);
}
