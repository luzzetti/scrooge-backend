package lan.scrooge.api.infrastructure.web._debug;

import lan.scrooge.api._shared.exceptions.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/debug-tools/errors")
public class ErrorRestController {

  @GetMapping("/{errorCode}")
  public ResponseEntity<Void> launchException(@PathVariable Integer errorCode) {

    switch (errorCode) {
      case 400 -> throw new ElementNotValidException(Errors.DEBUG_ERROR);
      case 404 -> throw new ElementNotFoundException(Errors.DEBUG_ERROR);
      case 422 -> throw new ElementNotProcessableException(Errors.DEBUG_ERROR);
      case 409 -> throw new ElementNotUniqueException(Errors.DEBUG_ERROR);
      case 500 -> throw new RuntimeException(Errors.DEBUG_ERROR.getCode());
      default -> throw new IllegalArgumentException("Invalid error code");
    }
  }

  @Getter
  @RequiredArgsConstructor
  private enum Errors implements ApplicationError {
    DEBUG_ERROR("debug.error.code");
    private final String code;
  }
}
