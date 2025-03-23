package lan.scrooge.api.infrastructure.web.bank_account;

import lombok.Data;

@Data
public final class BankAccountCreationRequest {
  private final String mnemonicName;
}
