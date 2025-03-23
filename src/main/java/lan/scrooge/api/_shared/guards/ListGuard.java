package lan.scrooge.api._shared.guards;

import java.util.List;

public class ListGuard extends BaseAbstractGuard<List<?>> {
  public ListGuard(List<?> value) {
    super(value);
  }

  /**
   * Checks if the value of a ListGuard is null or empty.
   *
   * @param ex The ApplicationException to be thrown if the value is null or empty.
   * @return The ListGuard.
   */
  public ListGuard againstNullOrEmpty(RuntimeException ex) {
    return against(this::isNullOrEmpty, ex);
  }

  /**
   * Checks if the value of a ListGuard does not contain multiple values. (Basically, checks if a
   * given list.size() > 1)
   *
   * @param ex The ApplicationException to be thrown if the value contains multiple values.
   * @return The ListGuard.
   */
  public ListGuard againstContainingMultipleValues(RuntimeException ex) {
    return against(this::containsMultipleValues, ex);
  }

  public ListGuard againstSmallerThanMinimumSize(int minimumSize, RuntimeException ex) {
    return against(() -> isSmallestThanMinimumSize(minimumSize), ex);
  }

  public ListGuard againstBiggerThanMaximumSize(int maximumSize, RuntimeException ex) {
    return against(() -> isHigherThanMaximumSize(maximumSize), ex);
  }

  private boolean isNullOrEmpty() {
    return value == null || value.isEmpty();
  }

  private boolean containsMultipleValues() {
    return value.size() > 1;
  }

  private boolean isSmallestThanMinimumSize(int minimumSize) {
    return value.size() < minimumSize;
  }

  private boolean isHigherThanMaximumSize(int maximumSize) {
    return value.size() > maximumSize;
  }
}
