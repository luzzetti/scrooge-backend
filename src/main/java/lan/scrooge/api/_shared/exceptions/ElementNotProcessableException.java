package lan.scrooge.api._shared.exceptions;

public class ElementNotProcessableException extends AbstractApplicationException {

  public ElementNotProcessableException(ApplicationError applicationError) {
    super(applicationError);
  }
}
