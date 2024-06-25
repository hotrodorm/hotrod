package org.hotrod.runtime.livesql.expressions;

import java.util.logging.Logger;

import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.ReferenceableExpression;

public class AliasedExpression extends Expression implements ReferenceableExpression {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(AliasedExpression.class.getName());

  private Expression referencedExpression;

  public AliasedExpression(final Expression referencedExpression, final String alias) {
    super(PRECEDENCE_ALIAS);
    this.referencedExpression = referencedExpression;
    super.register(this.referencedExpression);
    super.setAlias(alias);
    super.setTypeHandler(this.referencedExpression.getTypeHandler());
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    this.referencedExpression.renderTo(w);
    w.write(" as ");
    w.write(w.getSQLDialect().canonicalToNatural(super.getAlias()));
  }

  @Override
  public String getName() {
    return super.getAlias();
  }

  protected Expression getExpression() {
    return referencedExpression;
  }

}
