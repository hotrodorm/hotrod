package org.hotrod.livesql.expressions.bool;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class Not extends BooleanSyntaxExpression {

  private Predicate a;

  public Not(final Predicate a) {
    super(Expression.PRECEDENCE_NOT);
    this.a = a;
    super.register(this.a);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.write("not ");
    super.renderInner(this.a, w);
  }

}
