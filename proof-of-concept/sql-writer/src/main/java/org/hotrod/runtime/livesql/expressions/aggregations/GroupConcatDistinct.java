package org.hotrod.runtime.livesql.expressions.aggregations;

import java.util.List;

import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;

public class GroupConcatDistinct extends NonWindowableAggregationFunction<String> {

  private List<OrderingTerm> ordering;
  private Expression<String> separator;

  public GroupConcatDistinct(final Expression<String> expression, final List<OrderingTerm> ordering,
      final Expression<String> separator) {
    super("group_concat", "distinct", expression);
    this.ordering = ordering;
    this.separator = separator;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().groupConcat(w, true, (Expression<String>) super.expression, this.ordering,
        this.separator);
  }

}
