package lan.scrooge.api._shared.guards;

/**
 * The StringGuard class is a specialized guard that provides validation functionalities for
 * strings. It extends the BaseGuard class and takes a String value as input.
 *
 * <p>Example Usage: String value = "Hello World";
 *
 * <p>String result = StringGuard.guard(value) .againstNullOrWhitespace(new
 * ElementNotValidException(GenericErrors.GUARD_STOPPED_NULL_OR_WHITESPACE)
 * .againstUnsafeSlashes(new ElementNotValidException(GenericErrors.MY_ERROR));
 *
 * <p>System.out.println(result); // Output: "Hello World"
 */
public class StringGuard extends BaseAbstractGuard<String> {

  public StringGuard(String value) {
    super(value);
  }

  public StringGuard againstNullOrWhitespace(RuntimeException ex) {
    return against(this::isNullOrWhitespace, ex);
  }

  public StringGuard againstUnsafeSlashes(RuntimeException ex) {
    return against(this::containsUnsafeSlashes, ex);
  }

  private boolean isNullOrWhitespace() {
    return value == null || value.isBlank();
  }

  private boolean containsUnsafeSlashes() {
    return value != null && (value.contains("/") || value.contains("\\"));
  }
}
