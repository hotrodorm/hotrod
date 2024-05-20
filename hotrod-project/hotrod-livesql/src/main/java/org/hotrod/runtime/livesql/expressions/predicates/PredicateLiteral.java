package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public class PredicateLiteral extends Predicate {

  private static final PredicateLiteral FALSE = new PredicateLiteral(false);
  private static final PredicateLiteral TRUE = new PredicateLiteral(true);

  private boolean value;

  public PredicateLiteral(final boolean value) {
    super(Expression.PRECEDENCE_LITERAL);
    this.value = value;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    if (this.value) {
      w.getSQLDialect().getBooleanLiteralRenderer().renderTrue(w);
    } else {
      w.getSQLDialect().getBooleanLiteralRenderer().renderFalse(w);
    }
  }

  // Getters

  public static PredicateLiteral getFalse() {
    return FALSE;
  }

  public static PredicateLiteral getTrue() {
    return TRUE;
  }

}
