package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.expressions.object.ObjectExpression;
import org.hotrod.livesql.expressions.object.ObjectFunction;
import org.hotrod.livesql.queries.QueryWriter;

public class ObjectLag extends ObjectFunction implements PositionalAnalyticFunction {

  private ObjectExpression expression;
  private NumericExpression offset;
  private ObjectExpression defaultValue;

  public ObjectLag(final ObjectExpression expression) {
    super(expression);
    this.expression = expression;
    this.offset = null;
    this.defaultValue = null;
  }

  public ObjectLag(final ObjectExpression expression, final NumericExpression offset) {
    super(expression, offset);
    this.expression = expression;
    this.offset = offset;
    this.defaultValue = null;
  }

  public ObjectLag(final ObjectExpression expression, final NumericExpression offset,
      final ObjectExpression defaultValue) {
    super(expression, offset, defaultValue);
    this.expression = expression;
    this.offset = offset;
    this.defaultValue = defaultValue;
  }

  public ObjectWindowFunctionOverStage over() {
    return new ObjectWindowFunctionOverStage(new ObjectWindowExpression(this));
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
