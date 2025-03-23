package lan.scrooge.api.domain.vos;

import java.util.UUID;
import lan.scrooge.api._shared.exceptions.ApplicationError;
import lan.scrooge.api._shared.exceptions.ElementNotValidException;
import lan.scrooge.api._shared.guards.Guards;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class ScroogeUserId {

  private final UUID value;

  public ScroogeUserId(UUID value) {
    Guards.guard(value).againstNull(new ElementNotValidException(Errors.NOT_VALID_ID_NULL));
    this.value = value;
  }

  public static ScroogeUserId of(UUID value) {
    return new ScroogeUserId(value);
  }

  public static ScroogeUserId of(String value) {
    return new ScroogeUserId(UUID.fromString(value));
  }

  public String asText() {
    return this.getValue().toString();
  }

  public static ScroogeUserId generate() {
    return new ScroogeUserId(UUID.randomUUID());
  }

  @Getter
  @RequiredArgsConstructor
  private enum Errors implements ApplicationError {
    NOT_VALID_ID_NULL("not-valid.id.null");
    private final String code;
  }
}
