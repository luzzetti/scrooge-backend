package lan.scrooge.api.infrastructure.web._shared;

import java.net.URI;
import lan.scrooge.api._shared.exceptions.ElementNotFoundException;
import lan.scrooge.api._shared.exceptions.ElementNotProcessableException;
import lan.scrooge.api._shared.exceptions.ElementNotUniqueException;
import lan.scrooge.api._shared.exceptions.ElementNotValidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ElementNotValidException.class)
  public ProblemDetail handleElementNotValid(ElementNotValidException e) {
    var aProblem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    aProblem.setTitle("Invalid");
    aProblem.setDetail(e.getMessage());
    aProblem.setInstance(URI.create(e.getMessage()));
    return aProblem;
  }

  @ExceptionHandler(ElementNotFoundException.class)
  public ProblemDetail handleElementNotFound(ElementNotFoundException e) {
    var aProblem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    aProblem.setTitle("Not found");
    aProblem.setDetail(e.getMessage());
    aProblem.setInstance(URI.create(e.getMessage()));
    return aProblem;
  }

  @ExceptionHandler(ElementNotProcessableException.class)
  public ProblemDetail handleElementNotProcessable(ElementNotProcessableException e) {
    var aProblem = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
    aProblem.setTitle("Try again later");
    aProblem.setDetail(e.getMessage());
    aProblem.setInstance(URI.create(e.getMessage()));
    return aProblem;
  }

  @ExceptionHandler(ElementNotUniqueException.class)
  public ProblemDetail handleElementNotUnique(ElementNotUniqueException e) {
    var aProblem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
    aProblem.setTitle("Conflict");
    aProblem.setDetail(e.getMessage());
    aProblem.setInstance(URI.create(e.getMessage()));
    return aProblem;
  }

}
