package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.Helper;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public class EnclosedBooleanExpression extends Predicate {

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
    Helper.renderTo(this.expr, w);
    w.write(")");
  }

}
