package org.hotrod.livesql.expressions.bool;

import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class EnclosedBooleanExpression extends BooleanSyntaxExpression {

  // Properties

  private Predicate expr;

  // Constructor

  public EnclosedBooleanExpression(final Predicate expr) {
    super(Expression.PRECEDENCE_PARENTHESIS);
    if (expr == null) {
      throw new LiveSQLException("Enclosed expression cannot be null");
    }
    this.expr = expr;
  }

  // Rendering

  @Override
  protected void renderTo(final QueryWriter w) {
    w.write("(");
    Shield.renderTo(this.expr, w);
    w.write(")");
  }

}
