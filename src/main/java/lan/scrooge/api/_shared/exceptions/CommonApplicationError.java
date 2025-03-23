package lan.scrooge.api._shared.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonApplicationError implements ApplicationError {
  NOT_VALID_ID_NULL("domain.id.null");

  private final String code;
}
