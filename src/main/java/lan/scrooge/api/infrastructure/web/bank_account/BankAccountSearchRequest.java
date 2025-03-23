package lan.scrooge.api.infrastructure.web.bank_account;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BankAccountSearchRequest {
  private String text;
}
