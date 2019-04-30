package org.hotrod.runtime.sql.expressions.numbers;

public class Abs extends NumericFunction {

  public Abs(final NumberExpression a) {
    super("abs", a);
  }

}
