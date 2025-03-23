package lan.scrooge.api._shared.exceptions;

public class UserNotAuthorizedException extends AbstractApplicationException {
  public UserNotAuthorizedException(ApplicationError applicationError) {
    super(applicationError);
  }
}
