package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.expressions.strings.StringFunction;

public class BooleanLag extends StringFunction implements PositionalAnalyticFunction {

  public BooleanLag(final Predicate expression) {
    super("lag(#{})", expression);
  }

  public BooleanLag(final Predicate expression, final NumberExpression offset) {
    super("lag(#{}, #{})", expression, offset);
  }

  public BooleanLag(final Predicate expression, final NumberExpression offset, final Predicate defaultValue) {
    super("lag(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public BooleanWindowFunctionOverStage over() {
    return new BooleanWindowFunctionOverStage(new BooleanWindowExpression(this));
  }

}
