package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.bool.BooleanFunction;
import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class BooleanLead extends BooleanFunction implements PositionalAnalyticFunction {

  public BooleanLead(final Predicate expression) {
    super("lead(#{})", expression);
  }

  public BooleanLead(final Predicate expression, final NumericExpression offset) {
    super("lead(#{}, #{})", expression, offset);
  }

  public BooleanLead(final Predicate expression, final NumericExpression offset, final Predicate defaultValue) {
    super("lead(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public BooleanWindowFunctionOverStage over() {
    return new BooleanWindowFunctionOverStage(new BooleanWindowExpression(this));
  }

}
