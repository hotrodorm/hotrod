package org.hotrod.runtime.livesql.expressions.numbers;

import java.util.Arrays;
import java.util.List;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class NumberCoalesce extends NumberExpression {

  private List<NumberExpression> expressions;

  public NumberCoalesce(final NumberExpression... values) {
    super(Expression.PRECEDENCE_FUNCTION);
    this.expressions = Arrays.asList(values);
    this.expressions.forEach(e -> super.register(e));
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().coalesce(w, this.expressions);
  }

}
