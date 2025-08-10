package org.hotrod.livesql.queries.select;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.SQLExpression;
import org.hotrod.livesql.queries.LiveSQLContext;
import org.hotrod.livesql.queries.ctes.CTE;
import org.hotrod.livesql.queries.select.sets.BaseSelectObject;
import org.hotrod.livesql.queries.select.sets.IndividualSelectPhase;
import org.hotrod.livesql.queries.select.sets.MultiSet;
import org.hotrod.livesql.queries.select.tuples.SelectTuplesColumnsPhase;

public class SelectColumnsPhase<R> extends IndividualSelectPhase<R> {

  // Constructor

  public SelectColumnsPhase(final LiveSQLContext context, final List<CTE> ctes, final boolean distinct,
      final SQLExpression... resultSetColumns) {
    super(context, ctes, distinct, false);
    for (SQLExpression c : resultSetColumns) {
      if (c == null) {
        throw new LiveSQLException("Select column cannot be null.");
      }
    }
    MultiSet<R> m = this.combined.getLastSelect();
    UnarySelectObject<R> s = (UnarySelectObject<R>) m;
    s.setResultSetColumns(Arrays.asList(resultSetColumns).stream().collect(Collectors.toList()));
  }

//  // Conversion to tuples query
//
//  public SelectTuplesColumnsPhase tuples() {
//    BaseSelectObject<R> select = super.combined.getLastSelect();
//    List<SQLExpression> cols = select.getResultSetColumns();
//    SQLExpression[] colsa = cols == null ? null : cols.toArray(new SQLExpression[0]);
//    return new SelectTuplesColumnsPhase(this.context, select.getCTEs(), select.getDistinct(), colsa);
//  }

  // Next phases

  public SelectFromPhase<R> from(final TableExpression tableViewOrSubquery) {
    return new SelectFromPhase<R>(this.context, this.combined, tableViewOrSubquery);
  }

}
