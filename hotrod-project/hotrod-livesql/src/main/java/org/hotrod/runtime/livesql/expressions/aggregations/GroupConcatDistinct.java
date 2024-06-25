package org.hotrod.runtime.livesql.expressions.aggregations;

import java.util.List;

import org.hotrod.runtime.livesql.expressions.OrderingTerm;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
import org.hotrod.runtime.livesql.expressions.strings.StringFunction;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public class GroupConcatDistinct extends StringFunction implements NonWindowableAggregationFunction {

  private StringExpression expression;
  private List<OrderingTerm> ordering;
  private StringExpression separator;

  public GroupConcatDistinct(final StringExpression expression, final List<OrderingTerm> ordering,
      final StringExpression separator) {
    super("<custom-rendering>");
    this.ordering = ordering;
    this.expression = expression;
    this.separator = separator;
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().groupConcat(w, true, this.expression, this.ordering, this.separator);
  }

}
