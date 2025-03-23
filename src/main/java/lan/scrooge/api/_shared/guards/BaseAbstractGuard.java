package lan.scrooge.api._shared.guards;

import java.util.function.BooleanSupplier;

public abstract class BaseAbstractGuard<T> {

  protected final T value;

  protected BaseAbstractGuard(T value) {
    this.value = value;
  }

  protected <D extends BaseAbstractGuard<T>> D against(
      BooleanSupplier tester, RuntimeException ex) {
    if (tester.getAsBoolean()) {
      throw ex;
    }

    @SuppressWarnings("unchecked")
    var thisClass = (D) this;

    return thisClass;
  }

}
