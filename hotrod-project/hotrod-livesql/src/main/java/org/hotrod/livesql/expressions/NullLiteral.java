package org.hotrod.livesql.expressions;

import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.subqueries.Subquery;
import org.hotrod.livesql.queries.subqueries.SubqueryNullColumn;

public class NullLiteral extends ExistenceExpression {

  public NullLiteral() {
    super(Expression.PRECEDENCE_LITERAL);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.write("NULL");
  }

  @Override
  protected Expression asSubqueryExpression(Subquery subquery, String alias) {
    return new SubqueryNullColumn(subquery, alias);
  }

}
