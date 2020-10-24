package org.hotrod.runtime.livesql.expressions.aggregations;

import java.util.List;

import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
import org.hotrod.runtime.livesql.expressions.strings.StringFunction;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class GroupConcatDistinct extends StringFunction implements NonWindowableAggregationFunction {

  private List<OrderingTerm> ordering;
  private StringExpression separator;

  public GroupConcatDistinct(final StringExpression expression, final List<OrderingTerm> ordering,
      final StringExpression separator) {
    super("group_concat", "distinct", expression);
    this.ordering = ordering;
    this.separator = separator;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().groupConcat(w, true, (StringExpression) super.parameters, this.ordering,
        this.separator);
  }

}
