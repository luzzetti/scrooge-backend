package lan.scrooge.api.domain.services;

import java.util.Random;
import lan.scrooge.api.domain.vos.IBAN;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IbanGenerator {

  /*
    Esempi:
  IT32Z0300203280723759962198
  IT06P0300203280454574521642
  IT53V0300203280544348828559
  IT06 O0300 20328 05744 69847118
   */
  private static final String COUNTRY_CODE = "IT";
  private static final int IBAN_LENGTH = 27; // Fixed length for Italian IBAN

  private static final Random random = new Random();

  public static IBAN generateRandomIBAN() {
    StringBuilder ibanBuilder = new StringBuilder(COUNTRY_CODE);

    // Generate random control digits
    ibanBuilder.append(random.nextInt(90) + 10); // Two digits between 10 and 99

    // Append fixed bank and country-specific structure
    ibanBuilder.append("03002"); // Bank and branch identifier
    ibanBuilder.append("03280"); // National check digits and branch code

    // Generate remaining random account number (numeric part of IBAN)
    for (int i = 0; i < 13; i++) {
      ibanBuilder.append(random.nextInt(10)); // Random digit between 0 and 9
    }

    return new IBAN(ibanBuilder.toString());
  }
}
