package org.hotrod.livesql.expressions.bool;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class BooleanNullIf extends BooleanSyntaxExpression {

  private Predicate a;
  private Predicate b;

  public BooleanNullIf(final Predicate a, final Predicate b) {
    super(Expression.PRECEDENCE_FUNCTION);
    this.a = a;
    this.b = b;
    super.register(a);
    super.register(b);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().nullif(w, this.a, this.b);
  }

}
