package org.hotrod.runtime.sql.expressions.datetime;

public abstract class DateTimeFunction extends DateTimeExpression {

  private static final int PRECEDENCE = 1;

  protected DateTimeFunction() {
    super(PRECEDENCE);
  }

}
