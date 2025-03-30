package lan.scrooge.api.infrastructure.web.bank_account;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BankAccountResource {
  private String id;
  private String mnemonicName;
  private String iban;
  private BigDecimal balance;
}
