package org.hotrod.runtime.livesql.expressions.object;

import java.util.Arrays;
import java.util.List;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class ObjectCoalesce extends ObjectExpression {

  private List<Expression<Object>> expressions;

  public ObjectCoalesce(final ObjectExpression... values) {
    super(Expression.PRECEDENCE_FUNCTION);
    this.expressions.addAll(Arrays.asList(values));
    this.expressions.forEach(e -> super.register(e));
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().coalesce(w, this.expressions);
  }

}
