package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.predicates.BooleanFunction;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class BooleanLead extends BooleanFunction implements PositionalAnalyticFunction {

  public BooleanLead(final Predicate expression, final NumberExpression offset, final Predicate defaultValue) {
    super("lead", expression, offset, defaultValue);
  }

  public BooleanWindowFunctionOverStage over() {
    return new BooleanWindowFunctionOverStage(new WindowExpression(this));
  }

}
