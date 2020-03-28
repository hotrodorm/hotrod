package org.hotrod.runtime.livesql.expressions;

import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.ReferenceableExpression;

public class AliasedExpression implements ReferenceableExpression {

  private Expression<?> expression;
  private String alias;

  public AliasedExpression(final Expression<?> expression, final String alias) {
    this.expression = expression;
    this.alias = alias;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    this.expression.renderTo(w);
    w.write(" as ");
    w.write(w.getSqlDialect().getIdentifierRenderer().renderSQLName(this.alias));
  }

}
