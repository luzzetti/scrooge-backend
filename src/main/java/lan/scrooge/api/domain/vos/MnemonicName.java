package lan.scrooge.api.domain.vos;

import lan.scrooge.api._shared.exceptions.ApplicationError;
import lan.scrooge.api._shared.exceptions.ElementNotValidException;
import lan.scrooge.api._shared.guards.Guards;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class MnemonicName {

  private final String value;

  public MnemonicName(String value) {

    Guards.guard(value)
        .againstNullOrWhitespace(new ElementNotValidException(Errors.NOT_VALID_NAME_NULL));

    Guards.guard(value)
        .againstUnsafeSlashes(new ElementNotValidException(Errors.NOT_VALID_NAME_UNSAFE));

    // TODO: Check su lunghezza massima
    // TODO: Profanity Check?

    this.value = value;
  }

  public static MnemonicName of(String value) {
    return new MnemonicName(value);
  }

  @Getter
  @RequiredArgsConstructor
  private enum Errors implements ApplicationError {
    NOT_VALID_NAME_NULL("not-valid.name.null"),
    NOT_VALID_NAME_UNSAFE("not-valid.name.unsafe");
    private final String code;
  }
}
