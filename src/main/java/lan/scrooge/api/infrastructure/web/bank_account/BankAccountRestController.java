package lan.scrooge.api.infrastructure.web.bank_account;

import lan.scrooge.api.application.ports.input.CloseBankAccountUseCase;
import lan.scrooge.api.application.ports.input.CreateBankAccountUseCase;
import lan.scrooge.api.application.ports.input.ListBankAccountQuery;
import lan.scrooge.api.application.ports.input.ShowBankAccountQuery;
import lan.scrooge.api.domain.entities.BankAccount;
import lan.scrooge.api.domain.entities.ScroogeUser;
import lan.scrooge.api.domain.vos.BankAccountId;
import lan.scrooge.api.domain.vos.IBAN;
import lan.scrooge.api.domain.vos.MnemonicName;
import lan.scrooge.api.domain.vos.RemainingFundDestination;
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
  private final CloseBankAccountUseCase closeBankAccountUseCase;
  private final ListBankAccountQuery listBankAccountQuery;
  private final ShowBankAccountQuery showBankAccountQuery;

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

    BankAccountId bankAccountId = createBankAccountUseCase.create(command);

    var response = new BankAccountCreationResponse(bankAccountId.toString());
    return ResponseEntity.created(null).body(response);
  }

  // Non posso utilizzare un verbo Delete, in quanto ho dei dati nel body.
  // Devo quindi fallbackare ad un `custom method` che per convenzione è in POST
  @PostMapping("{bankAccountId}:close")
  @Override
  public ResponseEntity<Void> closeBankAccount(
      @AuthenticationPrincipal ScroogeUser principal,
      @PathVariable String bankAccountId,
      @RequestBody BankAccountClosureRequest request) {

    var destination = RemainingFundDestination.valueOf(request.destination());
    var commandBuilder =
        CloseBankAccountUseCase.CloseBankAccountCommand.builder()
            .currentUser(principal)
            .bankAccountId(BankAccountId.of(bankAccountId))
            .destination(destination);

    if (destination == RemainingFundDestination.IBAN) {
      commandBuilder.ibanDestination(new IBAN(request.ibanDestination()));
    }

    closeBankAccountUseCase.close(commandBuilder.build());

    return ResponseEntity.noContent().build();
  }

  @GetMapping
  @Override
  public ResponseEntity<ResourceList<BankAccountOverviewResource>> listBankAccounts(
      @AuthenticationPrincipal ScroogeUser principal, BankAccountSearchRequest request) {

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
                            .iban(r.getIban().getValue())
                            .mnemonicName(r.getMnemonicName().getValue())
                            .balance(r.getBalance())
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

    var criterion =
        ShowBankAccountQuery.ShowBankAccountCriterion.builder()
            .currentUser(principal)
            .bankAccountId(BankAccountId.of(bankAccountId))
            .build();

    BankAccount bankAccount = showBankAccountQuery.showBankAccount(criterion);

    var response =
        BankAccountResource.builder()
            .id(bankAccount.getId().asText())
            .mnemonicName(bankAccount.getMnemonicName().getValue())
            .iban(bankAccount.getIban().getValue())
            .balance(bankAccount.getBalance())
            .build();

    return ResponseEntity.ok(response);
  }
}
