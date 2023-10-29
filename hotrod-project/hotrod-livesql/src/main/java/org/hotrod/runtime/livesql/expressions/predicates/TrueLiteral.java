package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class TrueLiteral extends Predicate {

  private LiveSQLContext context;

  public TrueLiteral(final LiveSQLContext context) {
    super(Expression.PRECEDENCE_LITERAL);
    this.context = context;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    this.context.getLiveSQLDialect().getBooleanLiteralRenderer().renderTrue(this.context, w);
  }

}
