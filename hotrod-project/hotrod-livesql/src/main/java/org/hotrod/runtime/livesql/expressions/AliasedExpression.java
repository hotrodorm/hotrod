package org.hotrod.runtime.livesql.expressions;

import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
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
    w.write(w.getSqlDialect().canonicalToNatural(this.alias));
  }

  @Override
  public void validateTableReferences(TableReferences tableReferences, AliasGenerator ag) {
    this.expression.validateTableReferences(tableReferences, ag);
  }

}
