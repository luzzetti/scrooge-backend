package lan.scrooge.api.domain.services;

import static org.junit.jupiter.api.Assertions.*;

import lan.scrooge.api.domain.vos.IBAN;
import org.junit.jupiter.api.Test;

class IbanGeneratorTest {

  /**
   * Test class for the IbanGenerator service. This class ensures the "generateRandomIBAN" method
   * correctly generates valid IBAN objects.
   */
  @Test
  void generateRandomIBAN_shouldGenerateValidIBAN() {
    // Act
    IBAN generatedIban = IbanGenerator.generateRandomIBAN();

    // Assert
    assertNotNull(generatedIban, "Generated IBAN should not be null");
    assertTrue(
        isValidIBANFormat(generatedIban.getValue()), "Generated IBAN should have a valid format");
  }

  @Test
  void generateRandomIBAN_shouldStartWithCountryCode() {
    // Act
    IBAN generatedIban = IbanGenerator.generateRandomIBAN();

    // Assert
    assertNotNull(generatedIban, "Generated IBAN should not be null");
    assertTrue(
        generatedIban.getValue().startsWith("IT"), "IBAN should start with the country code 'IT'");
  }

  @Test
  void generateRandomIBAN_shouldHaveCorrectLength() {
    // Act
    IBAN generatedIban = IbanGenerator.generateRandomIBAN();

    // Assert
    assertNotNull(generatedIban, "Generated IBAN should not be null");
    assertEquals(
        27,
        generatedIban.getValue().length(),
        "Generated IBAN should have a length of 27 characters");
  }

  @Test
  void generateRandomIBAN_shouldContainFixedBankAndBranchCodes() {
    // Act
    IBAN generatedIban = IbanGenerator.generateRandomIBAN();

    // Assert
    assertNotNull(generatedIban, "Generated IBAN should not be null");
    String ibanString = generatedIban.getValue();
    assertTrue(
        ibanString.contains("0300203280"),
        "IBAN should contain the fixed bank and branch codes '0300203280'");
  }

  @Test
  void generateRandomIBAN_shouldGenerateDifferentIBANs() {
    // Act
    IBAN iban1 = IbanGenerator.generateRandomIBAN();
    IBAN iban2 = IbanGenerator.generateRandomIBAN();

    // Assert
    assertNotNull(iban1, "First generated IBAN should not be null");
    assertNotNull(iban2, "Second generated IBAN should not be null");
    assertNotEquals(
        iban1.getValue(), iban2.getValue(), "Two generated IBANs should not be identical");
  }

  // Helper method to verify IBAN format
  private boolean isValidIBANFormat(String iban) {
    return iban.matches("^IT\\d{2}0300203280\\d{13}$");
  }
}
