package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.numbers.GeneralNumberExpression;
import org.hotrod.runtime.livesql.expressions.predicates.GeneralBooleanExpression;
import org.hotrod.runtime.livesql.expressions.strings.StringFunction;

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
