package org.hotrod.runtime.livesql.expressions.datetime;

import java.util.Date;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeFieldExpression.DateTimeField;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;

public abstract class DateTimeExpression extends Expression<Date> {

  protected DateTimeExpression(final int precedence) {
    super(precedence);
  }

  // Coalesce

  public DateTimeExpression coalesce(final DateTimeExpression a) {
    return new DateTimeCoalesce(this, a);
  }

  public DateTimeExpression coalesce(final Date a) {
    return new DateTimeCoalesce(this, new DateTimeConstant(a));
  }

  // DateTime Functions

  public DateTimeExpression date() {
    return new org.hotrod.runtime.livesql.expressions.datetime.Date(this);
  }

  public DateTimeExpression time() {
    return new org.hotrod.runtime.livesql.expressions.datetime.Time(this);
  }

  public NumberExpression extract(final DateTimeField field) {
    return new Extract(this, new DateTimeFieldExpression(field));
  }

}
