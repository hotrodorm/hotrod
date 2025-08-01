package org.hotrod.livesql.expressions.aggregations;

import java.util.List;

import org.hotrod.livesql.expressions.character.GeneralCharExpression;
import org.hotrod.livesql.expressions.character.CharFunction;
import org.hotrod.livesql.ordering.OrderingTerm;
import org.hotrod.livesql.queries.QueryWriter;

public class GroupConcatDistinct extends CharFunction implements NonWindowableAggregationFunction {

  private GeneralCharExpression expression;
  private List<OrderingTerm> ordering;
  private GeneralCharExpression separator;

  public GroupConcatDistinct(final GeneralCharExpression expression, final List<OrderingTerm> ordering,
      final GeneralCharExpression separator) {
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
