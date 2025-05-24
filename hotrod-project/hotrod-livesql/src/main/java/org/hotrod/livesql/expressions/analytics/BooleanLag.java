package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.numbers.GeneralNumberExpression;
import org.hotrod.livesql.expressions.predicates.GeneralBooleanExpression;
import org.hotrod.livesql.expressions.strings.StringFunction;

public class BooleanLag extends StringFunction implements PositionalAnalyticFunction {

  public BooleanLag(final GeneralBooleanExpression expression) {
    super("lag(#{})", expression);
  }

  public BooleanLag(final GeneralBooleanExpression expression, final GeneralNumberExpression offset) {
    super("lag(#{}, #{})", expression, offset);
  }

  public BooleanLag(final GeneralBooleanExpression expression, final GeneralNumberExpression offset, final GeneralBooleanExpression defaultValue) {
    super("lag(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public BooleanWindowFunctionOverStage over() {
    return new BooleanWindowFunctionOverStage(new BooleanWindowExpression(this));
  }

}
