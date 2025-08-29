package org.hotrod.livesql.expressions.bool;

import java.util.Arrays;
import java.util.List;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class BooleanCoalesce extends BooleanSyntaxExpression {

  private List<Predicate> expressions;

  public BooleanCoalesce(final Predicate... values) {
    super(Expression.PRECEDENCE_FUNCTION);
    this.expressions = Arrays.asList(values);
    this.expressions.forEach(e -> super.register(e));
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().coalesce(w, this.expressions);
  }

}
