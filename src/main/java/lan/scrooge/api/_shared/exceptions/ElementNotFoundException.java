package lan.scrooge.api._shared.exceptions;

public class ElementNotFoundException extends AbstractApplicationException {

  public ElementNotFoundException(ApplicationError applicationError) {
    super(applicationError);
  }
}
