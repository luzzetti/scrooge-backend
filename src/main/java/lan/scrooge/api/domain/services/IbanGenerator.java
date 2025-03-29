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
    return new IBAN("IT25W0300203280913377155915");
  }
}
