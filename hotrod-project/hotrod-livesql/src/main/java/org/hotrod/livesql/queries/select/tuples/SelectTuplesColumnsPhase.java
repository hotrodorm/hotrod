package org.hotrod.livesql.queries.select.tuples;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.ResultSetColumn;
import org.hotrod.livesql.metadata.Table;
import org.hotrod.livesql.queries.LiveSQLContext;
import org.hotrod.livesql.queries.ctes.CTE;

public class SelectTuplesColumnsPhase {

  private TuplesMetadata metadata;

  public SelectTuplesColumnsPhase(final LiveSQLContext context, final List<CTE> ctes, final boolean distinct,
      final ResultSetColumn... resultSetColumns) {
    for (ResultSetColumn c : resultSetColumns) {
      if (c == null) {
        throw new LiveSQLException("Select column cannot be null.");
      }
    }
    List<ResultSetColumn> cols = Arrays.asList(resultSetColumns).stream().collect(Collectors.toList());
    this.metadata = new TuplesMetadata(context, ctes, distinct, cols);
  }

  public <T extends Table<A>, A> SelectTuplesFrom1Phase<A> from(T t) {
    this.metadata.from(t);
    return new SelectTuplesFrom1Phase<A>(this.metadata);
  }

}
