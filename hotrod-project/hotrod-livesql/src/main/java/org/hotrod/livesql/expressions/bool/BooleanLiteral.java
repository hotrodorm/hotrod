package org.hotrod.livesql.expressions.bool;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;

public class BooleanLiteral extends Predicate {

  protected static final BooleanLiteral FALSE = new BooleanLiteral(false);
  protected static final BooleanLiteral TRUE = new BooleanLiteral(true);

  private boolean value;

  public BooleanLiteral(final boolean value) {
    super(Expression.PRECEDENCE_LITERAL);
    this.value = value;
  }

  public static BooleanLiteral getFalse() {
    return FALSE;
  }

  public static BooleanLiteral getTrue() {
    return TRUE;
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    if (this.value) {
      w.getSQLDialect().getBooleanLiteralRenderer().renderTrue(w);
    } else {
      w.getSQLDialect().getBooleanLiteralRenderer().renderFalse(w);
    }
  }

}
