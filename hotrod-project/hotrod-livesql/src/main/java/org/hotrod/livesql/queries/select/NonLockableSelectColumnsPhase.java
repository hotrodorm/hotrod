package org.hotrod.livesql.queries.select;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.ResultSetColumn;
import org.hotrod.livesql.queries.LiveSQLContext;
import org.hotrod.livesql.queries.ctes.CTE;
import org.hotrod.livesql.queries.select.sets.IndividualSelectPhase;
import org.hotrod.livesql.queries.select.sets.MultiSet;

public class NonLockableSelectColumnsPhase<R> extends IndividualSelectPhase<R> {

  // Constructor

  public NonLockableSelectColumnsPhase(final LiveSQLContext context, final List<CTE> ctes, final boolean distinct,
      final ResultSetColumn... resultSetColumns) {
    super(context, ctes, distinct, false);
    for (ResultSetColumn c : resultSetColumns) {
      if (c == null) {
        throw new LiveSQLException("Select columns cannot be null.");
      }
    }
    MultiSet<R> m = this.combined.getLastSelect();
    UnarySelectObject<R> s = (UnarySelectObject<R>) m;
    s.setResultSetColumns(Arrays.asList(resultSetColumns).stream().collect(Collectors.toList()));
  }

  public NonLockableSelectColumnsPhase(final LiveSQLContext context, final List<CTE> ctes,
      final Expression[] distinctOn, final ResultSetColumn... resultSetColumns) {
    super(context, ctes, distinctOn, false);
    for (ResultSetColumn c : resultSetColumns) {
      if (c == null) {
        throw new LiveSQLException("Select columns cannot be null.");
      }
    }
    MultiSet<R> m = this.combined.getLastSelect();
    UnarySelectObject<R> s = (UnarySelectObject<R>) m;
    s.setResultSetColumns(Arrays.asList(resultSetColumns).stream().collect(Collectors.toList()));
  }

  // Next phases

  public NonLockableSelectFromPhase<R> from(final TableExpression tableViewOrSubquery) {
    return new NonLockableSelectFromPhase<R>(this.context, this.combined, tableViewOrSubquery);
  }

}
