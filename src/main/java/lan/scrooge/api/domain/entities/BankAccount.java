package lan.scrooge.api.domain.entities;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lan.scrooge.api._shared.exceptions.ApplicationError;
import lan.scrooge.api._shared.exceptions.ElementNotValidException;
import lan.scrooge.api._shared.guards.Guards;
import lan.scrooge.api.domain.vos.BankAccountId;
import lan.scrooge.api.domain.vos.IBAN;
import lan.scrooge.api.domain.vos.MnemonicName;
import lombok.*;
import lombok.extern.log4j.Log4j2;

@Getter
@Builder(buildMethodName = "rawBuild", builderClassName = "BuilderWithValidation")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@Log4j2
public class BankAccount {

  private final BankAccountId id;
  private final MnemonicName mnemonicName;
  private final IBAN iban;
  private ScroogeUser owner;
  private BigDecimal balance;

  public boolean hasOwner(ScroogeUser user) {
    return this.owner.getId().equals(user.getId());
  }

  public boolean canWithdrawn(@NotNull BigDecimal amount) {
    return (balance.compareTo(amount) >= 0);
  }

  public void withdrawn(@NotNull BigDecimal amount) {
    balance = balance.subtract(amount);
  }

  public void deposit(@NotNull BigDecimal amount) {
    balance = balance.add(amount);
  }

  /*
   * https://github.com/projectlombok/lombok/issues/2477#issuecomment-1637065102
   */
  public static class BuilderWithValidation {
    public BankAccount build() {
      var thisInstance = rawBuild();

      // Validation during the construction of the object
      Guards.guard(thisInstance.id)
          .againstNull(new ElementNotValidException(Errors.NOT_VALID_ID_NULL));
      Guards.guard(thisInstance.owner)
          .againstNull(new ElementNotValidException(Errors.NOT_VALID_OWNER_NULL));

      return thisInstance;
    }
  }

  @Getter
  @RequiredArgsConstructor
  private enum Errors implements ApplicationError {
    NOT_VALID_ID_NULL("not-valid.id.null"),
    NOT_VALID_OWNER_NULL("not-valid.owner.null");
    private final String code;
  }
}
