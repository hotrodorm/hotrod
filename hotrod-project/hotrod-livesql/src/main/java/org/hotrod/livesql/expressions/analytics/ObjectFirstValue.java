package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.object.ObjectExpression;
import org.hotrod.livesql.expressions.object.ObjectFunction;
import org.hotrod.livesql.queries.QueryWriter;

public class ObjectFirstValue extends ObjectFunction implements PositionalAnalyticFunction {

  private ComparableExpression expression;

  public ObjectFirstValue(final ObjectExpression expression) {
    super(expression);
    this.expression = expression;
  }

  public ObjectWindowFunctionOverStage over() {
    return new ObjectWindowFunctionOverStage(new ObjectWindowExpression(this));
  }

  @Override
  protected void renderTo(QueryWriter w) {
    w.write("first_value(");
    w.write(this.expression);
    w.write(")");
  }

}
