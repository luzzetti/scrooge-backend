package lan.scrooge.api.infrastructure.web.bank_account;

import lan.scrooge.api.domain.entities.ScroogeUser;
import lan.scrooge.api.infrastructure.web._shared.ResourceList;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

public interface BankAccountRestApi {

  /**
   * Creates a new bank account for the specified user.
   *
   * @param principal the authenticated user for whom the bank account will be created
   * @param request the request containing the details for the bank account creation, such as the mnemonic name
   * @return a ResponseEntity containing the details of the newly created bank account, including its unique identifier
   */
  ResponseEntity<BankAccountCreationResponse> createBankAccount(
          ScroogeUser principal, BankAccountCreationRequest request);

  /**
   * Retrieves a list of bank accounts based on the provided search criteria.
   *
   * @param principal the authenticated user requesting the list of bank accounts
   * @param request the search request containing filtering criteria, such as a search text
   * @return a ResponseEntity containing a ResourceList of BankAccountOverviewResource, which includes details of the filtered bank accounts
   */
  ResponseEntity<ResourceList<BankAccountOverviewResource>> listBankAccounts(
      ScroogeUser principal, BankAccountSearchRequest request);

  /**
   * Retrieves the details of a specific bank account based on the given bank account ID.
   *
   * @param principal the authenticated user requesting the bank account details
   * @param bankAccountId the unique identifier of the bank account to be retrieved
   * @return a ResponseEntity containing the details of the bank account, encapsulated in a BankAccountResource object
   */
  ResponseEntity<BankAccountResource> showBankAccount(
      @AuthenticationPrincipal ScroogeUser principal, @PathVariable String bankAccountId);

}
