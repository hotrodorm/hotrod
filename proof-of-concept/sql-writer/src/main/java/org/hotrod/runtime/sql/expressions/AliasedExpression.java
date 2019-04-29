package org.hotrod.runtime.sql.expressions;

import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.ReferenceableExpression;

public class AliasedExpression implements ReferenceableExpression {

  private Expression expression;
  private String alias;

  public AliasedExpression(final Expression expression, final String alias) {
    this.expression = expression;
    this.alias = alias;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    this.expression.renderTo(w);
    w.write(" as ");
    w.write(w.getSqlDialect().renderName(this.alias));
  }

}
