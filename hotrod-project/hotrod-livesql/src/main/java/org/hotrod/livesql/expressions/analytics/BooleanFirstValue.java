package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.bool.BooleanFunction;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class BooleanFirstValue extends BooleanFunction implements PositionalAnalyticFunction {

  private ComparableExpression expression;

  public BooleanFirstValue(final Predicate expression) {
    super(expression);
    this.expression = expression;
  }

  public BooleanWindowFunctionOverStage over() {
    return new BooleanWindowFunctionOverStage(new BooleanWindowExpression(this));
  }

  @Override
  protected void renderTo(QueryWriter w) {
    w.write("first_value(");
    w.write(this.expression);
    w.write(")");
  }

}
