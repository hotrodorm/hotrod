package org.hotrod.livesql.expressions.aggregations;

import java.util.List;

import org.hotrod.livesql.expressions.analytics.CharWindowExpression;
import org.hotrod.livesql.expressions.analytics.CharWindowFunctionOverStage;
import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.character.GeneralCharExpression;
import org.hotrod.livesql.expressions.character.CharFunction;
import org.hotrod.livesql.ordering.OrderingTerm;
import org.hotrod.livesql.queries.QueryWriter;

public class GroupConcat extends CharFunction implements WindowableAggregationFunction {

  private GeneralCharExpression expression;
  private List<OrderingTerm> ordering;
  private GeneralCharExpression separator;

  public GroupConcat(final GeneralCharExpression expression, final List<OrderingTerm> ordering,
      final GeneralCharExpression separator) {
    super("<custom-rendering>");
    this.ordering = ordering;
    this.expression = expression;
    this.separator = separator;
  }

  public CharWindowFunctionOverStage over() {
    return new CharWindowFunctionOverStage(new CharWindowExpression(this));
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().groupConcat(w, false, this.expression, this.ordering, this.separator);
  }

}
