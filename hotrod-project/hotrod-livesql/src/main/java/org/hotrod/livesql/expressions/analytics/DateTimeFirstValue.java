package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.livesql.expressions.datetime.DateTimeFunction;
import org.hotrod.livesql.queries.QueryWriter;

public class DateTimeFirstValue extends DateTimeFunction implements PositionalAnalyticFunction {

  private ComparableExpression expression;

  public DateTimeFirstValue(final DateTimeExpression expression) {
    super(expression);
    this.expression = expression;
  }

  public DateTimeWindowFunctionOverStage over() {
    return new DateTimeWindowFunctionOverStage(new DateTimeWindowExpression(this));
  }

  @Override
  protected void renderTo(QueryWriter w) {
    w.write("first_value(");
    w.write(this.expression);
    w.write(")");
  }

}
