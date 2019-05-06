package org.hotrod.runtime.sql.expressions.aggregations;

import java.util.List;

import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.expressions.Expression;
import org.hotrod.runtime.sql.ordering.OrderingTerm;

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
