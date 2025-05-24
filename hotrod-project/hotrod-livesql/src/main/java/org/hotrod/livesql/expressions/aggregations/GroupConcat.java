package org.hotrod.livesql.expressions.aggregations;

import java.util.List;

import org.hotrod.livesql.expressions.analytics.StringWindowExpression;
import org.hotrod.livesql.expressions.analytics.StringWindowFunctionOverStage;
import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.strings.GeneralStringExpression;
import org.hotrod.livesql.expressions.strings.StringFunction;
import org.hotrod.livesql.ordering.OrderingTerm;
import org.hotrod.livesql.queries.QueryWriter;

public class GroupConcat extends StringFunction implements WindowableAggregationFunction {

  private GeneralStringExpression expression;
  private List<OrderingTerm> ordering;
  private GeneralStringExpression separator;

  public GroupConcat(final GeneralStringExpression expression, final List<OrderingTerm> ordering,
      final GeneralStringExpression separator) {
    super("<custom-rendering>");
    this.ordering = ordering;
    this.expression = expression;
    this.separator = separator;
  }

  public StringWindowFunctionOverStage over() {
    return new StringWindowFunctionOverStage(new StringWindowExpression(this));
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().groupConcat(w, false, this.expression, this.ordering, this.separator);
  }

}
