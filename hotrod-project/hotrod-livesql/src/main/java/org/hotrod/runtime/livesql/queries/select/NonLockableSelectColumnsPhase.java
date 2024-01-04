package org.hotrod.runtime.livesql.queries.select;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.ctes.CTE;
import org.hotrod.runtime.livesql.queries.select.sets.IndividualSelectPhase;
import org.hotrod.runtime.livesql.queries.select.sets.MultiSet;

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
    SelectObject<R> s = (SelectObject<R>) m;
    s.setResultSetColumns(Arrays.asList(resultSetColumns).stream().collect(Collectors.toList()));
  }

  // Next phases

  public NonLockableSelectFromPhase<R> from(final TableExpression tableViewOrSubquery) {
    return new NonLockableSelectFromPhase<R>(this.context, this.combined, tableViewOrSubquery);
  }

}
