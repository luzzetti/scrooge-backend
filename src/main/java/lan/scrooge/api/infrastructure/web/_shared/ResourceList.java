package lan.scrooge.api.infrastructure.web._shared;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResourceList<T> {
  private List<T> results;
  private int pageNumber;
  private int pageSize;
  private int totalElements;
}
