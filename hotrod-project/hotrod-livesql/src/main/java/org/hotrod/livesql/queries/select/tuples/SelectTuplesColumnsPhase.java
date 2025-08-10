package org.hotrod.livesql.queries.select.tuples;

import java.util.Arrays;
import java.util.List;

import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.SQLExpression;
import org.hotrod.livesql.metadata.TableOrView;
import org.hotrod.livesql.queries.LiveSQLContext;
import org.hotrod.livesql.queries.ctes.CTE;
import org.hotrod.livesql.queries.subqueries.Subquery;

public class SelectTuplesColumnsPhase {

  private TuplesMetadata metadata;

  public SelectTuplesColumnsPhase(final LiveSQLContext context, final List<CTE> ctes, final boolean distinct,
      final SQLExpression... resultSetColumns) {
    for (SQLExpression c : resultSetColumns) {
      if (c == null) {
        throw new LiveSQLException("A select column cannot be null.");
      }
    }
    List<SQLExpression> cols = Arrays.asList(resultSetColumns);
    this.metadata = new TuplesMetadata(context, ctes, distinct, cols);
  }

  public <T extends TableOrView<A>, A> SelectTuplesFrom1Phase<A> from(T t) {
    this.metadata.from(t);
    return new SelectTuplesFrom1Phase<A>(this.metadata);
  }

  public SelectTuplesFrom0Phase from(Subquery t) {
    this.metadata.from(t);
    return new SelectTuplesFrom0Phase(this.metadata);
  }

}
