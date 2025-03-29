package lan.scrooge.api.infrastructure.web.bank_account;

import lan.scrooge.api.application.ports.input.CreateBankAccountUseCase;
import lan.scrooge.api.application.ports.input.ListBankAccountQuery;
import lan.scrooge.api.domain.entities.ScroogeUser;
import lan.scrooge.api.domain.vos.BankAccountId;
import lan.scrooge.api.domain.vos.MnemonicName;
import lan.scrooge.api.infrastructure.web._shared.ResourceList;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("1.0/bank-accounts")
@RequiredArgsConstructor
@Log4j2
public class BankAccountRestController implements BankAccountRestApi {

  private final CreateBankAccountUseCase createBankAccountUseCase;
  private final ListBankAccountQuery listBankAccountQuery;

  @PostMapping
  @Override
  public ResponseEntity<BankAccountCreationResponse> createBankAccount(
      @AuthenticationPrincipal ScroogeUser principal,
      @RequestBody BankAccountCreationRequest request) {

    var command =
        CreateBankAccountUseCase.CreateBankAccountCommand.builder()
            .currentUser(principal)
            .mnemonicName(MnemonicName.of(request.getMnemonicName()))
            .build();

    BankAccountId bankAccountId = createBankAccountUseCase.execute(command);

    var response = new BankAccountCreationResponse(bankAccountId.toString());
    return ResponseEntity.created(null).body(response);
  }

  @GetMapping
  @Override
  public ResponseEntity<ResourceList<BankAccountOverviewResource>> listBankAccounts(
      @AuthenticationPrincipal ScroogeUser principal, BankAccountSearchRequest request) {

    log.info("listBankAccounts: %s");

    var criterion =
        ListBankAccountQuery.ListBankAccountCriterion.builder().currentUser(principal).build();

    var result = listBankAccountQuery.listBankAccount(criterion);

    var response =
        ResourceList.<BankAccountOverviewResource>builder()
            .results(
                result.mapResults(
                    r ->
                        BankAccountOverviewResource.builder()
                            .id(r.getId().asText())
                            .mnemonicName(r.getMnemonicName().getValue())
                            .build()))
            .pageNumber(result.getPageNumber())
            .pageSize(result.getPageSize())
            .totalElements(result.getTotalElements())
            .build();

    return ResponseEntity.ok(response);
  }

  @GetMapping("/{bankAccountId}")
  @Override
  public ResponseEntity<BankAccountResource> showBankAccount(
      @AuthenticationPrincipal ScroogeUser principal, @PathVariable String bankAccountId) {

    log.info("showBankAccount: %s");

    return ResponseEntity.ok().build();
  }
}
