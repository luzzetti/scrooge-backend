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
public class TransactionId {

  private final UUID value;

  public TransactionId(UUID value) {
    Guards.guard(value).againstNull(new ElementNotValidException(Errors.NOT_VALID_ID_NULL));
    this.value = value;
  }

  public static TransactionId of(UUID value) {
    return new TransactionId(value);
  }

  public static TransactionId of(String value) {
    return new TransactionId(UUID.fromString(value));
  }

  public String asText() {
    return this.getValue().toString();
  }

  public static TransactionId generate() {
    return new TransactionId(UUID.randomUUID());
  }

  @Getter
  @RequiredArgsConstructor
  private enum Errors implements ApplicationError {
    NOT_VALID_ID_NULL("not-valid.id.null");
    private final String code;
  }
}
