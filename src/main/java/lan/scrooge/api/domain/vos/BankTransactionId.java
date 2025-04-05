package lan.scrooge.api.domain.vos;

import java.util.UUID;
import lan.scrooge.api._shared.exceptions.ApplicationError;
import lan.scrooge.api._shared.exceptions.ElementNotValidException;
import lan.scrooge.api._shared.guards.Guards;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
public class BankTransactionId {

  private final UUID value;

  public BankTransactionId(UUID value) {
    Guards.guard(value).againstNull(new ElementNotValidException(Errors.NOT_VALID_ID_NULL));
    this.value = value;
  }

  public static BankTransactionId of(UUID value) {
    return new BankTransactionId(value);
  }

  public static BankTransactionId of(String value) {
    return new BankTransactionId(UUID.fromString(value));
  }

  public String asText() {
    return this.getValue().toString();
  }

  public static BankTransactionId generate() {
    return new BankTransactionId(UUID.randomUUID());
  }

  @Getter
  @RequiredArgsConstructor
  private enum Errors implements ApplicationError {
    NOT_VALID_ID_NULL("not-valid.id.null");
    private final String code;
  }
}
