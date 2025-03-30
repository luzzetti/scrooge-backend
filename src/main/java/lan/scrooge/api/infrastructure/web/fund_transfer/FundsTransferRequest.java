package lan.scrooge.api.infrastructure.web.fund_transfer;

import java.math.BigDecimal;
import lombok.Data;

@Data
public final class FundsTransferRequest {
  private final String sourceIban;
  private final String targetIban;
  private final BigDecimal amount;
}
