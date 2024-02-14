package org.hotrod.runtime.livesql.queries.select;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.metadata.Entity;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.ctes.CTE;
import org.hotrod.runtime.livesql.queries.select.entity.SelectFromEntityPhase;
import org.hotrod.runtime.livesql.queries.select.sets.CombinedSelectObject;
import org.hotrod.runtime.livesql.queries.select.sets.IndividualSelectPhase;
import org.hotrod.runtime.livesql.queries.select.sets.MultiSet;

public class SelectColumnsPhase<R> extends IndividualSelectPhase<R> {

  // Constructor

  public SelectColumnsPhase(final LiveSQLContext context, final List<CTE> ctes, final boolean distinct,
      final ResultSetColumn... resultSetColumns) {
    super(context, ctes, distinct, false);
    for (ResultSetColumn c : resultSetColumns) {
      if (c == null) {
        throw new LiveSQLException("Select column cannot be null.");
      }
    }
    MultiSet<R> m = this.combined.getLastSelect();
    SelectObject<R> s = (SelectObject<R>) m;
    s.setResultSetColumns(Arrays.asList(resultSetColumns).stream().collect(Collectors.toList()));
  }

  // Next phases

  public SelectFromPhase<R> from(final TableExpression tableViewOrSubquery) {
    return new SelectFromPhase<R>(this.context, this.combined, tableViewOrSubquery);
  }

  // <T extends Table<A>, A> Select1<A>

  public <T extends Entity<A>, A> SelectFromEntityPhase<A> from(final T entity) {
//    return new SelectFromEntityPhase<A>(this.context, this.combined, null, null);
    CombinedSelectObject<A> combinedA = (CombinedSelectObject<A>) this.combined;
    return new SelectFromEntityPhase<A>(context, combinedA, null, entity.getModel());
  }

}
