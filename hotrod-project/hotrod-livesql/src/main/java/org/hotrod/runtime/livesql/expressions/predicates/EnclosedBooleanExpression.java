package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class EnclosedBooleanExpression extends Predicate {

  // Properties

  private Predicate expr;

  // Constructor

  public EnclosedBooleanExpression(final Predicate expr) {
    super(Expression.PRECEDENCE_PARENTHESIS);
    this.expr = expr;
  }

  // Rendering

  @Override
  public void renderTo(final QueryWriter w) {
    w.write("(");
    this.expr.renderTo(w);
    w.write(")");
  }

}
