package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.character.CharFunction;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class BooleanFirstValue extends CharFunction implements PositionalAnalyticFunction {

  public BooleanFirstValue(final Predicate expression) {
    super("first_value(#{})", expression);
  }

  public BooleanWindowFunctionOverStage over() {
    return new BooleanWindowFunctionOverStage(new BooleanWindowExpression(this));
  }

}
