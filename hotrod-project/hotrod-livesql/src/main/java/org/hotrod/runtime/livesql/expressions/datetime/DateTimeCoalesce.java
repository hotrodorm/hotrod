package org.hotrod.runtime.livesql.expressions.datetime;

import java.util.Arrays;
import java.util.List;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public class DateTimeCoalesce extends DateTimeExpression {

  private List<DateTimeExpression> expressions;

  public DateTimeCoalesce(final DateTimeExpression... values) {
    super(Expression.PRECEDENCE_FUNCTION);
    this.expressions = Arrays.asList(values);
    this.expressions.forEach(e -> super.register(e));
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().coalesce(w, this.expressions);
  }

}
