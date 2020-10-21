package org.hotrod.runtime.livesql.expressions.datetime;

import java.util.Date;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.general.Constant;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class DateTimeConstant extends DateTimeExpression {

  private Constant<Date> constant;

  public DateTimeConstant(final Date value) {
    super(Expression.PRECEDENCE_LITERAL);
    this.constant = new Constant<Date>(value);
    super.register(this.constant);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    this.constant.renderTo(w);
  }

}
