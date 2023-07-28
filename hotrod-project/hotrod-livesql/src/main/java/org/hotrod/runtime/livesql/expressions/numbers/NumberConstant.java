package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class NumberConstant extends NumberExpression {

  // Properties

  private Number value;
  private boolean parameterize;

  // Constructor

  public NumberConstant(final Number value) {
    super(Expression.PRECEDENCE_LITERAL);
    this.parameterize = true;
//    this.parameterize = value != null && (this.isFloat(value) || this.isDouble(value));
    this.value = value;
  }

  // Utilities

  private boolean isFloat(final Number n) {
    try {
      Float.class.cast(n);
      return true;
    } catch (ClassCastException e) {
      return false;
    }
  }

  private boolean isDouble(final Number n) {
    try {
      Double.class.cast(n);
      return true;
    } catch (ClassCastException e) {
      return false;
    }
  }

  // Rendering

  @Override
  public void renderTo(final QueryWriter w) {
    if (this.parameterize) {
      String name = w.registerParameter(this.value);
      w.write("#{" + name);
      w.write("}");
    } else {
      w.write("" + this.value);
    }
  }

}
