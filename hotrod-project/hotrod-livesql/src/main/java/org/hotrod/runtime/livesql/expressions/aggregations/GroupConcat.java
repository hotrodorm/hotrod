package org.hotrod.runtime.livesql.expressions.aggregations;

import java.util.List;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class GroupConcat extends StringAggregationFunction {

  private List<OrderingTerm> ordering;
  private Expression<String> separator;

  public GroupConcat(final Expression<String> expression, final List<OrderingTerm> ordering,
      final Expression<String> separator) {
    super("group_concat", expression);
    this.ordering = ordering;
    this.separator = separator;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().groupConcat(w, false, super.expression, this.ordering, this.separator);
  }

}
