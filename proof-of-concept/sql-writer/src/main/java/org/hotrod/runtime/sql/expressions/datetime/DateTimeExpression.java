package org.hotrod.runtime.sql.expressions.datetime;

import java.util.Date;

import org.hotrod.runtime.sql.expressions.Expression;
import org.hotrod.runtime.sql.expressions.datetime.DateTimeFieldExpression.DateTimeField;

public abstract class DateTimeExpression extends Expression<Date> {

  protected DateTimeExpression(final int precedence) {
    super(precedence);
  }

  public DateTimeExpression date() {
    return new org.hotrod.runtime.sql.expressions.datetime.Date(this);
  }

  public DateTimeExpression time() {
    return new org.hotrod.runtime.sql.expressions.datetime.Time(this);
  }

  public DateTimeExpression extract(final DateTimeField field) {
    return new Extract(this, new DateTimeFieldExpression(field));
  }

}
