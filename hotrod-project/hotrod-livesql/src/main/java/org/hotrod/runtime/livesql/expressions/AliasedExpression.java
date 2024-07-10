package org.hotrod.runtime.livesql.expressions;

import java.util.List;
import java.util.logging.Logger;

import org.hotrod.runtime.livesql.queries.QueryWriter;

public class AliasedExpression extends Expression {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(AliasedExpression.class.getName());

  private Expression referencedExpression;
  private String alias;

  public AliasedExpression(final Expression referencedExpression, final String alias) {
    super(PRECEDENCE_ALIAS);
    this.referencedExpression = referencedExpression;
    this.alias = alias;
    super.register(this.referencedExpression);
  }

  public String getReferenceName() {
    return this.alias;
  }

  @Override
  public void captureTypeHandler() {
    this.referencedExpression.captureTypeHandler();
    log.info("captureTypeHandler: this.referencedExpression@" + +System.identityHashCode(this.referencedExpression)
        + ": " + this.referencedExpression);
    super.setTypeHandler(this.referencedExpression.getTypeHandler());
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    this.referencedExpression.renderTo(w);
    w.write(" as ");
    w.write(w.getSQLDialect().canonicalToNatural(this.alias));
  }

  protected List<Expression> unwrap() {
    return referencedExpression.unwrap();
  }

  public String toString() {
    return "'" + this.alias + "' for " + this.referencedExpression.toString();
  }

}
