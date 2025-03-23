package lan.scrooge.api._shared.exceptions;

import java.io.Serializable;

public interface ApplicationError extends Serializable {
  String getCode();
}
