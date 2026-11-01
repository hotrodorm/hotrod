package org.hotrod.livesql.queries;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.metadata.EntityColumn;

public class Assignment {

  private EntityColumn c;
  private Expression e;

  public Assignment(final EntityColumn c, final Expression e) {
    this.c = c;
    this.e = e;
  }

  public EntityColumn getColumn() {
    return c;
  }

  public Expression getExpression() {
    return e;
  }

}
