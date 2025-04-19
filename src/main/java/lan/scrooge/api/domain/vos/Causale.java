package lan.scrooge.api.domain.vos;

import lan.scrooge.api._shared.exceptions.ApplicationError;
import lan.scrooge.api._shared.exceptions.ElementNotValidException;
import lan.scrooge.api._shared.guards.Guards;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class Causale {

  private static final int MAX_LENGTH = 150;
  private final String value;

  public Causale(String value) {
    Guards.guard(value).againstNullOrWhitespace(new ElementNotValidException(Errors.NOT_VALID_CAUSALE_NULL));
    if (value.length() > MAX_LENGTH) {
      throw new ElementNotValidException(Errors.NOT_VALID_CAUSALE_TOO_LONG);
    }
    this.value = value;
  }

  public static Causale of(String value) {
    return new Causale(value);
  }

  public String asText() {
    return this.getValue();
  }

  @Getter
  @RequiredArgsConstructor
  private enum Errors implements ApplicationError {
    NOT_VALID_CAUSALE_NULL("not-valid.causale.null"),
    NOT_VALID_CAUSALE_TOO_LONG("not-valid.causale.too-long");
    private final String code;
  }
}
