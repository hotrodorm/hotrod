package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.object.ObjectExpression;
import org.hotrod.livesql.expressions.object.ObjectFunction;
import org.hotrod.livesql.queries.QueryWriter;

public class ObjectLastValue extends ObjectFunction implements PositionalAnalyticFunction {

  private ComparableExpression expression;

  public ObjectLastValue(final ObjectExpression expression) {
    super(expression);
    this.expression = expression;
  }

  public ObjectWindowFunctionOverStage over() {
    return new ObjectWindowFunctionOverStage(new ObjectWindowExpression(this));
  }

  @Override
  protected void renderTo(QueryWriter w) {
    w.write("last_value(");
    w.write(this.expression);
    w.write(")");
  }

}
