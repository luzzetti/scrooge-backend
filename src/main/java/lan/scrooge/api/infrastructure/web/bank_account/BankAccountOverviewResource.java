package lan.scrooge.api.infrastructure.web.bank_account;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BankAccountOverviewResource {
  private String id;
  private String mnemonicName;
  // Aggiungere altri dati come il saldo totale
}
