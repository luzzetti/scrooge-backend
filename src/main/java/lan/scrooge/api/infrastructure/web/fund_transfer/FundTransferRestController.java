package lan.scrooge.api.infrastructure.web.fund_transfer;

import jakarta.validation.constraints.NotNull;
import lan.scrooge.api.application.ports.input.ListBankTransactionsQuery;
import lan.scrooge.api.application.ports.input.ListUserTransactionsQuery;
import lan.scrooge.api.application.ports.input.TransferFundsUseCase;
import lan.scrooge.api.domain.entities.BankTransaction;
import lan.scrooge.api.domain.entities.ScroogeUser;
import lan.scrooge.api.domain.vos.BankAccountId;
import lan.scrooge.api.domain.vos.BankTransactionId;
import lan.scrooge.api.domain.vos.IBAN;
import lan.scrooge.api.infrastructure.jpa.UserTransactionProjection;
import lan.scrooge.api.infrastructure.web._shared.ResourceList;
import lan.scrooge.api.infrastructure.web._shared.TransactionType;
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
  private final ListBankTransactionsQuery listBankTransactionsQuery;
  private final ListUserTransactionsQuery listUserTransactionsQuery;

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

    BankTransactionId transactionId = transferFundsUseCase.transfer(command);

    var response = new FundsTransferResponse(transactionId.asText());
    return ResponseEntity.ok(response);
  }

  @GetMapping
  @Override
  public ResponseEntity<ResourceList<BankTransactionOverviewResource>> listBankTransactions(
      @AuthenticationPrincipal ScroogeUser principal, BankTransactionSearchRequest request) {

    var criterion =
        ListBankTransactionsQuery.ListBankTransactionsCriterion.builder()
            .currentUser(principal)
            .bankAccountId(BankAccountId.of(request.bankAccountId()))
            .build();

    var results = listBankTransactionsQuery.listBankTransactions(criterion);

    var response =
        ResourceList.<BankTransactionOverviewResource>builder()
            .results(
                results.mapResults(
                    t ->
                        BankTransactionOverviewResource.builder()
                            .id(t.getId().asText())
                            .transactionType(evaluateTransactionType(t, request.bankAccountId()))
                            .amount(t.getAmount())
                            .createdAt(t.getCreatedAt())
                            .build()))
            .pageNumber(results.getPageNumber())
            .pageSize(results.getPageSize())
            .totalElements(results.getTotalElements())
            .build();

    return ResponseEntity.ok(response);
  }

  @GetMapping("/user")
  @Override
  public ResponseEntity<ResourceList<UserTransactionProjection>> listUserTransactions(
      @AuthenticationPrincipal ScroogeUser principal, UserTransactionSearchRequest request) {

    var criterion =
        ListUserTransactionsQuery.ListUserTransactionsCriterion.builder()
            .currentUser(principal)
            .build();

    var results = listUserTransactionsQuery.listUserTransactions(criterion);

    var response =
        ResourceList.<UserTransactionProjection>builder()
            .results(results.getResults())
            .pageNumber(results.getPageNumber())
            .pageSize(results.getPageSize())
            .totalElements(results.getTotalElements())
            .build();

    return ResponseEntity.ok(response);
  }

  private TransactionType evaluateTransactionType(
      BankTransaction transaction, @NotNull String bankAccountId) {

    // Se l'ID che sto cercando è il "target" di una transazione, i fondi gli sono stati accreditati
    // Viceversa, gli sono stati addebitati
    var isIncome = transaction.getTargetAccountId().equals(BankAccountId.of(bankAccountId));
    return (isIncome) ? TransactionType.INCOME : TransactionType.EXPENSE;
  }
}
