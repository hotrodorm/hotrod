package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.character.CharExpression;
import org.hotrod.livesql.expressions.character.CharFunction;
import org.hotrod.livesql.queries.QueryWriter;

public class CharLastValue extends CharFunction implements PositionalAnalyticFunction {

  private ComparableExpression expression;

  public CharLastValue(final CharExpression expression) {
    super(expression);
    this.expression = expression;
  }

  public CharWindowFunctionOverStage over() {
    return new CharWindowFunctionOverStage(new CharWindowExpression(this));
  }

  @Override
  protected void renderTo(QueryWriter w) {
    w.write("last_value(");
    w.write(this.expression);
    w.write(")");
  }

}
