package lan.scrooge.api._shared.guards;

import java.util.List;

public interface Guards {

  static StringGuard guard(String value) {
    return new StringGuard(value);
  }

  static ListGuard guard(List<?> value) {
    return new ListGuard(value);
  }
  
  static ObjectGuard guard(Object value) {
    return new ObjectGuard(value);
  }
}
