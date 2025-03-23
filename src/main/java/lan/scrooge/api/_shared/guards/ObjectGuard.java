package lan.scrooge.api._shared.guards;

public class ObjectGuard extends BaseAbstractGuard<Object> {
  public ObjectGuard(Object value) {
    super(value);
  }

  public ObjectGuard againstNull(RuntimeException ex) {
    return against(this::isNullOrEmpty, ex);
  }

  private boolean isNullOrEmpty() {
    return value == null;
  }
}
