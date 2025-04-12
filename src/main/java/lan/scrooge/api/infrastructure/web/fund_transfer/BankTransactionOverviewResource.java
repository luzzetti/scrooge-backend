package lan.scrooge.api.infrastructure.web.fund_transfer;

import java.math.BigDecimal;
import java.time.Instant;
import lan.scrooge.api.infrastructure.web._shared.TransactionType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BankTransactionOverviewResource {

  private String id;
  private TransactionType transactionType;
  private BigDecimal amount;
  private Instant createdAt;
}
