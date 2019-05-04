package org.hotrod.runtime.sql.expressions.strings;

public abstract class StringFunction extends StringExpression {

  private static final int PRECEDENCE = 1;

  protected StringFunction() {
    super(PRECEDENCE);
  }

}
