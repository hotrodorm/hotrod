package org.hotrod.runtime.livesql.expressions.numbers;

public abstract class NumericFunction extends NumberExpression {

  private static final int PRECEDENCE = 1;

  protected NumericFunction() {
    super(PRECEDENCE);
  }

}
