package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.binary.BinaryExpression;
import org.hotrod.livesql.expressions.binary.BinaryFunction;
import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.queries.QueryWriter;

public class BinaryLag extends BinaryFunction implements PositionalAnalyticFunction {

  private BinaryExpression expression;
  private NumericExpression offset;
  private BinaryExpression defaultValue;

  public BinaryLag(final BinaryExpression expression) {
    super(expression);
    this.expression = expression;
    this.offset = null;
    this.defaultValue = null;
  }

  public BinaryLag(final BinaryExpression expression, final NumericExpression offset) {
    super(expression, offset);
    this.expression = expression;
    this.offset = offset;
    this.defaultValue = null;
  }

  public BinaryLag(final BinaryExpression expression, final NumericExpression offset,
      final BinaryExpression defaultValue) {
    super(expression, offset, defaultValue);
    this.expression = expression;
    this.offset = offset;
    this.defaultValue = defaultValue;
  }

  public BinaryWindowFunctionOverStage over() {
    return new BinaryWindowFunctionOverStage(new BinaryWindowExpression(this));
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
