package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public class PredicateLiteral extends Predicate {

  protected static final PredicateLiteral FALSE = new PredicateLiteral(false);
  protected static final PredicateLiteral TRUE = new PredicateLiteral(true);

  private boolean value;

  public PredicateLiteral(final boolean value) {
    super(Expression.PRECEDENCE_LITERAL);
    this.value = value;
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
