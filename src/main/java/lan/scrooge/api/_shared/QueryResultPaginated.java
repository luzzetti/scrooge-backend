package lan.scrooge.api._shared;

import java.util.List;
import java.util.function.Function;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QueryResultPaginated<T> {
  private List<T> results;
  private int pageNumber;
  private int pageSize;
  private int totalElements;

  public <R> List<R> mapResults(Function<T,R> mapper) {
    return results.stream()
            .map(mapper)
            .toList();
  }
}
