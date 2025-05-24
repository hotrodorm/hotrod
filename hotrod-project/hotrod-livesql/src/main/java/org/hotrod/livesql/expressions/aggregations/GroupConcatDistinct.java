package org.hotrod.livesql.expressions.aggregations;

import java.util.List;

import org.hotrod.livesql.expressions.strings.GeneralStringExpression;
import org.hotrod.livesql.expressions.strings.StringFunction;
import org.hotrod.livesql.ordering.OrderingTerm;
import org.hotrod.livesql.queries.QueryWriter;

public class GroupConcatDistinct extends StringFunction implements NonWindowableAggregationFunction {

  private GeneralStringExpression expression;
  private List<OrderingTerm> ordering;
  private GeneralStringExpression separator;

  public GroupConcatDistinct(final GeneralStringExpression expression, final List<OrderingTerm> ordering,
      final GeneralStringExpression separator) {
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
