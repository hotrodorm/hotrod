package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.character.CharFunction;
import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class BooleanLag extends CharFunction implements PositionalAnalyticFunction {

  public BooleanLag(final Predicate expression) {
    super("lag(#{})", expression);
  }

  public BooleanLag(final Predicate expression, final NumericExpression offset) {
    super("lag(#{}, #{})", expression, offset);
  }

  public BooleanLag(final Predicate expression, final NumericExpression offset, final Predicate defaultValue) {
    super("lag(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public BooleanWindowFunctionOverStage over() {
    return new BooleanWindowFunctionOverStage(new BooleanWindowExpression(this));
  }

}
