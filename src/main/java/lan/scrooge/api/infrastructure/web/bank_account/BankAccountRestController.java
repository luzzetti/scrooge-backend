package lan.scrooge.api.infrastructure.web.bank_account;

import java.util.Collections;
import lan.scrooge.api.domain.entities.ScroogeUser;
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

  @PostMapping
  @Override
  public ResponseEntity<BankAccountCreationResponse> createBankAccount(
      @AuthenticationPrincipal ScroogeUser principal,
      @RequestBody BankAccountCreationRequest request) {

    log.info("createBankAccount: %s");

    return ResponseEntity.created(null).body(null);
  }

  @GetMapping
  @Override
  public ResponseEntity<ResourceList<BankAccountOverviewResource>> listBankAccounts(
      @AuthenticationPrincipal ScroogeUser principal, BankAccountSearchRequest request) {

    log.info("listBankAccounts: %s");

    var response =
        ResourceList.<BankAccountOverviewResource>builder()
            .results(Collections.emptyList())
            .resultsCount(0)
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
