package lan.scrooge.api.domain.entities;

import lan.scrooge.api._shared.exceptions.ApplicationError;
import lan.scrooge.api._shared.exceptions.ElementNotValidException;
import lan.scrooge.api._shared.guards.Guards;
import lan.scrooge.api.domain.vos.Email;
import lan.scrooge.api.domain.vos.ScroogeUserId;
import lombok.*;

@Getter
@Builder(buildMethodName = "rawBuild", builderClassName = "BuilderWithValidation")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class ScroogeUser {

  private ScroogeUserId id;
  private Email email;

  /*
   * https://github.com/projectlombok/lombok/issues/2477#issuecomment-1637065102
   */
  public static class BuilderWithValidation {
    public ScroogeUser build() {
      var thisInstance = rawBuild();

      // Validation during the construction of the object
      Guards.guard(thisInstance.id)
          .againstNull(new ElementNotValidException(Errors.NOT_VALID_ID_NULL));
      Guards.guard(thisInstance.email)
          .againstNull(new ElementNotValidException(Errors.NOT_VALID_EMAIL_NULL));

      return thisInstance;
    }
  }

  @Getter
  @RequiredArgsConstructor
  private enum Errors implements ApplicationError {
    NOT_VALID_ID_NULL("not-valid.id.null"),
    NOT_VALID_EMAIL_NULL("not-valid.email.null");
    private final String code;
  }
}
