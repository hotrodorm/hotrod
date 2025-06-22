package org.hotrod.livesql.expressions;

import java.util.List;
import java.util.logging.Logger;

import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.subqueries.Subquery;

public class AliasedExpression extends Expression {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(AliasedExpression.class.getName());

  private Expression referencedExpression;
  private String alias;

  public AliasedExpression(final Expression referencedExpression, final String alias) {
    super(referencedExpression);
    this.referencedExpression = referencedExpression;
    this.alias = alias;
    super.register(this.referencedExpression);
  }

  protected Expression asSubqueryExpression(final Subquery subquery, final String alias) {
    return this.referencedExpression.asSubqueryExpression(subquery, alias);
  }

  // TypeHandler setter

  public TypedExpression type(final Class<?> type) {
    return new TypedExpression(this, type);
  }

  // Rendering

  protected String getReferenceName() {
    log.info("############### this.alias=" + this.alias);
    return this.alias;
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    this.referencedExpression.renderTo(w);
    w.write(" as ");
    w.write(w.getSQLDialect().canonicalToNatural(this.alias));
  }

  protected List<Expression> expand() {
    return referencedExpression.expand();
  }

  protected String render() {
    return "'" + this.alias + "' for " + this.referencedExpression.toString();
  }

}
