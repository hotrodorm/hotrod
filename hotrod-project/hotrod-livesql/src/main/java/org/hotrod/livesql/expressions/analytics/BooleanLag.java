package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.bool.BooleanFunction;
import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class BooleanLag extends BooleanFunction implements PositionalAnalyticFunction {

  private Predicate expression;
  private NumericExpression offset;
  private Predicate defaultValue;

  public BooleanLag(final Predicate expression) {
    super(expression);
    this.expression = expression;
    this.offset = null;
    this.defaultValue = null;
  }

  public BooleanLag(final Predicate expression, final NumericExpression offset) {
    super(expression, offset);
    this.expression = expression;
    this.offset = offset;
    this.defaultValue = null;
  }

  public BooleanLag(final Predicate expression, final NumericExpression offset, final Predicate defaultValue) {
    super(expression, offset, defaultValue);
    this.expression = expression;
    this.offset = offset;
    this.defaultValue = defaultValue;
  }

  public BooleanWindowFunctionOverStage over() {
    return new BooleanWindowFunctionOverStage(new BooleanWindowExpression(this));
  }

  @Override
  protected void renderTo(QueryWriter w) {
    w.write("lag(");
    w.write(this.expression);
    if (this.offset != null) {
      w.write(", ");
      w.write(this.offset);
    }
    if (this.defaultValue != null) {
      w.write(", ");
      w.write(this.defaultValue);
    }
    w.write(")");
  }

}
