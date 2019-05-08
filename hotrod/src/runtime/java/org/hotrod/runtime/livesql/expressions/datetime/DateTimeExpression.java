package org.hotrod.runtime.livesql.expressions.datetime;

import java.util.Date;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeFieldExpression.DateTimeField;

public abstract class DateTimeExpression extends Expression<Date> {

  protected DateTimeExpression(final int precedence) {
    super(precedence);
  }

  public DateTimeExpression date() {
    return new org.hotrod.runtime.livesql.expressions.datetime.Date(this);
  }

  public DateTimeExpression time() {
    return new org.hotrod.runtime.livesql.expressions.datetime.Time(this);
  }

  public DateTimeExpression extract(final DateTimeField field) {
    return new Extract(this, new DateTimeFieldExpression(field));
  }

}
