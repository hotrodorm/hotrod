package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.numeric.NumericFunction;
import org.hotrod.livesql.queries.QueryWriter;

public class Rank extends NumericFunction implements AnalyticFunction {

  private ComparableExpression expression;

  public Rank(final ComparableExpression expression) {
    super(expression);
    this.expression = expression;
  }

  public NumericWindowFunctionOverStage over() {
    return new NumericWindowFunctionOverStage(new NumericWindowExpression(this));
  }

  @Override
  protected void renderTo(QueryWriter w) {
    w.write("rank(");
    w.write(this.expression);
    w.write(")");
  }

}
