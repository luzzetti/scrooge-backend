package lan.scrooge.api.domain.vos;

import java.util.regex.Pattern;
import lan.scrooge.api._shared.exceptions.ApplicationError;
import lan.scrooge.api._shared.exceptions.ElementNotValidException;
import lan.scrooge.api._shared.guards.Guards;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class Email {

  private static final String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
  private static final Pattern emailPattern = Pattern.compile(emailRegex);

  private final String value;

  public Email(String value) {
    Guards.guard(value)
        .againstNullOrWhitespace(new ElementNotValidException(Errors.NOT_VALID_EMAIL_NULL));

    if (!emailPattern.matcher(value).matches()) {
      throw new ElementNotValidException(Errors.NOT_VALID_EMAIL_FORMAT);
    }

    this.value = value;
  }

  public static Email of(String value) {
    return new Email(value);
  }

  @Getter
  @RequiredArgsConstructor
  private enum Errors implements ApplicationError {
    NOT_VALID_EMAIL_NULL("not-valid.email.null"),
    NOT_VALID_EMAIL_FORMAT("not-valid.email.format");
    private final String code;
  }
}
