package lan.scrooge.api.infrastructure.web.bank_account;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BankAccountOverviewResource {
  private String id;
  private String iban;
  private String mnemonicName;
  private BigDecimal balance;
}
