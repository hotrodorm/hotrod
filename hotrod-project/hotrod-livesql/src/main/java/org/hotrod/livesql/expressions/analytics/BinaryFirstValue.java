package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.binary.BinaryExpression;
import org.hotrod.livesql.expressions.binary.BinaryFunction;
import org.hotrod.livesql.queries.QueryWriter;

public class BinaryFirstValue extends BinaryFunction implements PositionalAnalyticFunction {

  private ComparableExpression expression;

  public BinaryFirstValue(final BinaryExpression expression) {
    super(expression);
    this.expression = expression;
  }

  public BinaryWindowFunctionOverStage over() {
    return new BinaryWindowFunctionOverStage(new BinaryWindowExpression(this));
  }

  @Override
  protected void renderTo(QueryWriter w) {
    w.write("first_value(");
    w.write(this.expression);
    w.write(")");
  }

}
