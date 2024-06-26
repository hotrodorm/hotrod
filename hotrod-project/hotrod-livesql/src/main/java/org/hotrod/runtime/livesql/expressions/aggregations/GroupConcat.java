package org.hotrod.runtime.livesql.expressions.aggregations;

import java.util.List;

import org.hotrod.runtime.livesql.expressions.analytics.StringWindowExpression;
import org.hotrod.runtime.livesql.expressions.analytics.StringWindowFunctionOverStage;
import org.hotrod.runtime.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
import org.hotrod.runtime.livesql.expressions.strings.StringFunction;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public class GroupConcat extends StringFunction implements WindowableAggregationFunction {

  private StringExpression expression;
  private List<OrderingTerm> ordering;
  private StringExpression separator;

  public GroupConcat(final StringExpression expression, final List<OrderingTerm> ordering,
      final StringExpression separator) {
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
