package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.character.CharFunction;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class BooleanLastValue extends CharFunction implements PositionalAnalyticFunction {

  public BooleanLastValue(final Predicate expression) {
    super("last_value(#{})", expression);
  }

  public BooleanWindowFunctionOverStage over() {
    return new BooleanWindowFunctionOverStage(new BooleanWindowExpression(this));
  }

}
