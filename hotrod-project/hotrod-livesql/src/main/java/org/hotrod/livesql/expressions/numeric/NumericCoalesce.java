package org.hotrod.livesql.expressions.numeric;

import java.util.Arrays;
import java.util.List;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;

public class NumericCoalesce extends NumericSyntaxExpression {

  private List<NumericExpression> expressions;

  public NumericCoalesce(final NumericExpression... values) {
    super(Expression.PRECEDENCE_FUNCTION);
    this.expressions = Arrays.asList(values);
    this.expressions.forEach(e -> super.register(e));
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().coalesce(w, this.expressions);
  }

}
