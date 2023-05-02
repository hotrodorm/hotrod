package org.hotrod.runtime.livesql.expressions;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.ReferenceableExpression;

public class AliasedExpression implements ReferenceableExpression {

  private Expression expression;
  private String alias;
  private boolean verbatim;

  public AliasedExpression(final Expression expression, final String alias) {
    this.expression = expression;
    this.alias = alias;
    this.verbatim = false;
  }

  public AliasedExpression(final Expression expression, final String alias, final boolean verbatim) {
    this.expression = expression;
    this.alias = alias;
    this.verbatim = verbatim;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    this.expression.renderTo(w);
    w.write(" as ");
    if (this.verbatim) {
      w.write(w.getSqlDialect().getIdentifierRenderer().renderSQLIdentifier(this.alias));
    } else {
      w.write(w.getSqlDialect().getIdentifierRenderer().renderSQLIdentifier(this.alias));
    }
  }

}
