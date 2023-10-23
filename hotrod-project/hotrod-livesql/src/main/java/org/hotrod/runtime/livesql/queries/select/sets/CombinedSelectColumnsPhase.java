package org.hotrod.runtime.livesql.queries.select.sets;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.ordering.CombinedOrderingTerm;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.ctes.CTE;
import org.hotrod.runtime.livesql.queries.select.SelectObject;
import org.hotrod.runtime.livesql.queries.select.TableExpression;

public class CombinedSelectColumnsPhase<R> extends CombinedSelectPhase<R> {

  // Constructor

  public CombinedSelectColumnsPhase(final LiveSQLContext context, final List<CTE> ctes, final boolean distinct,
      final CombinedSelectObject<R> cs, final ResultSetColumn... resultSetColumns) {
    super(context, new SelectObject<R>(ctes, distinct, false));
    this.select.setParent(cs);
    this.select.setResultSetColumns(Arrays.asList(resultSetColumns).stream().collect(Collectors.toList()));
  }

  // Next phases

  public CombinedSelectFromPhase<R> from(final TableExpression tableViewOrSubquery) {
    return new CombinedSelectFromPhase<R>(this.context, this.select, tableViewOrSubquery);
  }

  // Combined selects can use ORDER BY, OFFSET, and LIMIT without a FROM clause

  public CombinedSelectOrderByPhase<R> orderBy(final CombinedOrderingTerm... orderingTerms) {
    return new CombinedSelectOrderByPhase<R>(this.context, this.select, orderingTerms);
  }

  public CombinedSelectOffsetPhase<R> offset(final int offset) {
    return new CombinedSelectOffsetPhase<R>(this.context, this.select, offset);
  }

  public CombinedSelectLimitPhase<R> limit(final int limit) {
    return new CombinedSelectLimitPhase<R>(this.context, this.select, limit);
  }

}
