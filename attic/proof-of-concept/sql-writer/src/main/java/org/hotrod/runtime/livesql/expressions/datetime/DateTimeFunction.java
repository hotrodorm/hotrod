package org.hotrod.runtime.livesql.expressions.datetime;

public abstract class DateTimeFunction extends DateTimeExpression {

  private static final int PRECEDENCE = 1;

  protected DateTimeFunction() {
    super(PRECEDENCE);
  }

}
