package lan.scrooge.api.infrastructure.web.fund_transfer;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BankTransactionOverviewResource {

  enum TransactionType {
    INCOME,
    EXPENSE
  }

  private String id;
  private TransactionType transactionType;
  private BigDecimal amount;
  private Instant createdAt;
}
