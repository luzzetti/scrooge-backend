package lan.scrooge.api._shared.exceptions;

public class ElementNotUniqueException extends AbstractApplicationException {
  public ElementNotUniqueException(ApplicationError applicationError) {
    super(applicationError);
  }
}
