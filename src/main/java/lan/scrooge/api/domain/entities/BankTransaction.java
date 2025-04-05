package lan.scrooge.api.domain.entities;

import java.math.BigDecimal;
import java.time.Instant;
import lan.scrooge.api._shared.exceptions.ApplicationError;
import lan.scrooge.api._shared.exceptions.ElementNotValidException;
import lan.scrooge.api._shared.guards.Guards;
import lan.scrooge.api.domain.vos.BankAccountId;
import lan.scrooge.api.domain.vos.BankTransactionId;
import lombok.*;
import lombok.extern.log4j.Log4j2;

@Getter
@Builder(buildMethodName = "rawBuild", builderClassName = "BuilderWithValidation")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@Log4j2
public class BankTransaction {
  private final BankTransactionId id;
  private final BankAccountId sourceAccountId;
  private final BankAccountId targetAccountId;
  private final BigDecimal amount;
  @Builder.Default private final Instant createdAt = Instant.now();

  /*
   * https://github.com/projectlombok/lombok/issues/2477#issuecomment-1637065102
   */
  public static class BuilderWithValidation {
    public BankTransaction build() {
      var thisInstance = rawBuild();

      // Validation during the construction of the object
      Guards.guard(thisInstance.id)
          .againstNull(new ElementNotValidException(Errors.NOT_VALID_ID_NULL));
      Guards.guard(thisInstance.sourceAccountId)
          .againstNull(new ElementNotValidException(Errors.NOT_VALID_SOURCE_ACCOUNT_NULL));
      Guards.guard(thisInstance.targetAccountId)
          .againstNull(new ElementNotValidException(Errors.NOT_VALID_TARGET_ACCOUNT_NULL));
      Guards.guard(thisInstance.amount)
          .againstNull(new ElementNotValidException(Errors.NOT_VALID_AMOUNT));
      Guards.guard(thisInstance.createdAt)
          .againstNull(new ElementNotValidException(Errors.INCOHERENT_TRANSACTION_TIMESTAMP));

      if (thisInstance.amount.compareTo(BigDecimal.ZERO) < 0) {
        throw new ElementNotValidException(Errors.NOT_VALID_AMOUNT_MUST_BE_POSITIVE);
      }

      return thisInstance;
    }
  }

  @Getter
  @RequiredArgsConstructor
  private enum Errors implements ApplicationError {
    NOT_VALID_ID_NULL("not-valid.id.null"),
    NOT_VALID_SOURCE_ACCOUNT_NULL("not-valid.source.account.null"),
    NOT_VALID_TARGET_ACCOUNT_NULL("not-valid.target.account.null"),
    NOT_VALID_AMOUNT("not-valid.amount.null"),
    NOT_VALID_AMOUNT_MUST_BE_POSITIVE("not-valid.amount.must.be.positive"),
    INCOHERENT_TRANSACTION_TIMESTAMP("not-valid.transaction.timestamp.incoherent");
    private final String code;
  }
}
