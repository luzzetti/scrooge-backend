package lan.scrooge.api.domain.vos;

import lan.scrooge.api._shared.exceptions.ElementNotValidException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CausaleTest {

    @Test
    void whenValueIsValid_ShouldCreateCausale() {
        // Arrange
        String validValue = "Valid causale";

        // Act
        Causale causale = Causale.of(validValue);

        // Assert
        assertEquals(validValue, causale.getValue());
        assertEquals(validValue, causale.asText());
    }

    @Test
    void whenValueIsNull_ShouldThrowException() {
        // Act & Assert
        assertThrows(ElementNotValidException.class, () -> Causale.of(null));
    }

    @Test
    void whenValueIsEmpty_ShouldThrowException() {
        // Act & Assert
        assertThrows(ElementNotValidException.class, () -> Causale.of(""));
    }

    @Test
    void whenValueIsBlank_ShouldThrowException() {
        // Act & Assert
        assertThrows(ElementNotValidException.class, () -> Causale.of("   "));
    }

    @Test
    void whenValueIsTooLong_ShouldThrowException() {
        // Arrange
        String tooLongValue = "a".repeat(151);

        // Act & Assert
        assertThrows(ElementNotValidException.class, () -> Causale.of(tooLongValue));
    }

    @Test
    void whenValueIsMaxLength_ShouldCreateCausale() {
        // Arrange
        String maxLengthValue = "a".repeat(150);

        // Act
        Causale causale = Causale.of(maxLengthValue);

        // Assert
        assertEquals(maxLengthValue, causale.getValue());
    }
}