package org.hotrod.livesql.queries.select;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.livesql.expressions.SQLExpression;
import org.hotrod.livesql.metadata.TableOrView;
import org.hotrod.livesql.queries.LiveSQLContext;
import org.hotrod.livesql.queries.ctes.CTE;
import org.hotrod.livesql.queries.select.sets.AbstractSelectPhase;
import org.hotrod.livesql.queries.select.sets.MultiSet;

public class PGSelectColumnsPhase<R> extends AbstractSelectPhase<R> {

  // Constructor

  public PGSelectColumnsPhase(final LiveSQLContext context, final List<CTE> ctes, final boolean distinct,
      final SQLExpression... resultSetColumns) {
    super(context, ctes, distinct, false);
    MultiSet<R> m = this.combined.getLastSelect();
    UnarySelectObject<R> s = (UnarySelectObject<R>) m;
    s.setResultSetColumns(Arrays.asList(resultSetColumns).stream().collect(Collectors.toList()));
  }

  // Next stages

  public SelectFromPhase<R> from(final TableOrView t) {
    return new SelectFromPhase<R>(this.context, this.combined, t);
  }

}
