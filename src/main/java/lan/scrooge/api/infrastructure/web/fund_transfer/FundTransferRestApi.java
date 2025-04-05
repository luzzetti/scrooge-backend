package lan.scrooge.api.infrastructure.web.fund_transfer;

import lan.scrooge.api.domain.entities.ScroogeUser;
import lan.scrooge.api.infrastructure.web._shared.ResourceList;
import org.springframework.http.ResponseEntity;

public interface FundTransferRestApi {

  ResponseEntity<FundsTransferResponse> transferFunds(
      ScroogeUser principal, FundsTransferRequest request);

  ResponseEntity<ResourceList<BankTransactionOverviewResource>> listBankTransactions(
      ScroogeUser principal, BankTransactionSearchRequest request);
}
