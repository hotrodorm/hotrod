package org.hotrod.runtime.livesql.expressions;

import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.TableReferences;
import org.hotrod.runtime.livesql.queries.select.ReferenceableExpression;

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
    w.write(w.getSQLDialect().canonicalToNatural(this.alias));
  }

  @Override
  public void validateTableReferences(TableReferences tableReferences, AliasGenerator ag) {
    this.expression.validateTableReferences(tableReferences, ag);
  }

  @Override
  public String getName() {
    return this.alias;
  }

  protected Expression getExpression() {
    return expression;
  }

}
