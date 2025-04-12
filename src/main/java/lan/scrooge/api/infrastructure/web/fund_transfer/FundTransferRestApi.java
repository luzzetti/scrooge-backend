package lan.scrooge.api.infrastructure.web.fund_transfer;

import lan.scrooge.api.domain.entities.ScroogeUser;
import lan.scrooge.api.infrastructure.jpa.UserTransactionProjection;
import lan.scrooge.api.infrastructure.web._shared.ResourceList;
import org.springframework.http.ResponseEntity;

/**
 * This interface defines the REST API contract for fund transfer and transaction listing
 * operations.
 */
public interface FundTransferRestApi {

  /**
   * Transfers funds between bank accounts based on the provided details in the request.
   *
   * @param principal The authenticated user initiating the fund transfer.
   * @param request The request containing the source IBAN, target IBAN, and amount to be
   *     transferred.
   * @return A ResponseEntity containing a FundsTransferResponse with the transaction ID of the fund
   *     transfer.
   */
  ResponseEntity<FundsTransferResponse> transferFunds(
      ScroogeUser principal, FundsTransferRequest request);

  /**
   * Retrieves a paginated list of bank transactions for a specified bank account.
   *
   * @param principal The authenticated user requesting the transactions.
   * @param request The search request containing the ID of the bank account for which transactions
   *     are to be listed.
   * @return A ResponseEntity containing a ResourceList of BankTransactionOverviewResource elements,
   *     each representing an individual transaction overview.
   */
  ResponseEntity<ResourceList<BankTransactionOverviewResource>> listBankTransactions(
      ScroogeUser principal, BankTransactionSearchRequest request);

  /**
   * Retrieves a paginated list of user-specific transactions based on the provided search criteria.
   *
   * @param principal The authenticated user requesting the transaction list.
   * @param request The search request containing criteria for filtering the user's transactions.
   * @return A ResponseEntity containing a ResourceList of BankTransactionOverviewResource elements,
   *     each representing an individual transaction overview.
   */
  ResponseEntity<ResourceList<UserTransactionProjection>> listUserTransactions(
      ScroogeUser principal, UserTransactionSearchRequest request);
}
