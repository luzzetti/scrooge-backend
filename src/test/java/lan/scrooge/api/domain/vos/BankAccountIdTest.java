package lan.scrooge.api.domain.vos;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import lan.scrooge.api._shared.exceptions.ElementNotValidException;
import org.junit.jupiter.api.Test;

class BankAccountIdTest {

  @Test
  void whenWrappedValueIsEquals_ShouldBeEqual() {
    BankAccountId id1 = BankAccountId.generate();
    BankAccountId id2 = BankAccountId.of(id1.getValue());

    assertEquals(id1, id2);
    assertEquals(id1.hashCode(), id2.hashCode());
  }

  /**
   * Tests the `of` static factory method which converts a UUID value into a `BankAccountId`
   * instance.
   */
  @Test
  void of_WithValidUUID_ShouldCreateBankAccountId() {
    UUID uuid = UUID.randomUUID();
    BankAccountId bankAccountId = BankAccountId.of(uuid);

    assertNotNull(bankAccountId);
    assertEquals(uuid, bankAccountId.getValue());
  }

  /**
   * Tests the `of` static factory method when given a valid string representation of a UUID for
   * creating a `BankAccountId`.
   */
  @Test
  void of_WithValidUUIDString_ShouldCreateBankAccountId() {
    UUID uuid = UUID.randomUUID();
    BankAccountId bankAccountId = BankAccountId.of(uuid.toString());

    assertNotNull(bankAccountId);
    assertEquals(uuid, bankAccountId.getValue());
  }

  /**
   * Tests the `of` static factory method when given a null UUID. Expects it to throw an
   * `ElementNotValidException`.
   */
  @Test
  void of_WithNullUUID_ShouldThrowException() {
    assertThrows(ElementNotValidException.class, () -> BankAccountId.of((UUID) null));
  }

  /**
   * Tests the `of` static factory method when given a null string representation of a UUID. Expects
   * it to throw a `NullPointerException`.
   */
  @Test
  void of_WithNullString_ShouldThrowException() {
    assertThrows(NullPointerException.class, () -> BankAccountId.of((String) null));
  }

  /**
   * Tests the `of` static factory method when given an invalid string representation of a UUID.
   * Expects it to throw an `IllegalArgumentException`.
   */
  @Test
  void of_WithInvalidUUIDString_ShouldThrowException() {
    String invalidUuid = "invalid-uuid";

    assertThrows(IllegalArgumentException.class, () -> BankAccountId.of(invalidUuid));
  }
}
