package org.hotrod.runtime.sql.expressions.dates;

import java.util.Date;

import org.hotrod.runtime.sql.expressions.Expression;

public abstract class DateExpression extends Expression<Date> {

  protected DateExpression(final int precedence) {
    super(precedence);
  }

}
