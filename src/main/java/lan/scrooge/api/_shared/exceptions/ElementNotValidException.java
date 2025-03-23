package lan.scrooge.api._shared.exceptions;

public class ElementNotValidException extends AbstractApplicationException {
  public ElementNotValidException(ApplicationError applicationError) {
    super(applicationError);
  }
}
