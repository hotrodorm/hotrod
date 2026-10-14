package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.livesql.expressions.datetime.DateTimeFunction;
import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.queries.QueryWriter;

public class DateTimeLag extends DateTimeFunction implements PositionalAnalyticFunction {

  private DateTimeExpression expression;
  private NumericExpression offset;
  private DateTimeExpression defaultValue;

  public DateTimeLag(final DateTimeExpression expression) {
    super(expression);
    this.expression = expression;
    this.offset = null;
    this.defaultValue = null;
  }

  public DateTimeLag(final DateTimeExpression expression, final NumericExpression offset) {
    super(expression, offset);
    this.expression = expression;
    this.offset = offset;
    this.defaultValue = null;
  }

  public DateTimeLag(final DateTimeExpression expression, final NumericExpression offset,
      final DateTimeExpression defaultValue) {
    super(expression, offset, defaultValue);
    this.expression = expression;
    this.offset = offset;
    this.defaultValue = defaultValue;
  }

  public DateTimeWindowFunctionOverStage over() {
    return new DateTimeWindowFunctionOverStage(new DateTimeWindowExpression(this));
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
