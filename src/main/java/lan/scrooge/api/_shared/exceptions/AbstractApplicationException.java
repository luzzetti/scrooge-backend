package lan.scrooge.api._shared.exceptions;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public abstract class AbstractApplicationException extends RuntimeException {

  private final ApplicationError applicationError;
  private final Map<String, Object> properties = new HashMap<>();

  /*** Short Exception
   * for a shorter exception: super(errorCode.getCode(), null, true, false);
   */
  protected AbstractApplicationException(ApplicationError applicationError) {
    super(applicationError.getCode());
    this.applicationError = applicationError;
  }

  public AbstractApplicationException appendInfo(String key, Object value) {
    properties.put(key, value);
    return this;
  }
}
