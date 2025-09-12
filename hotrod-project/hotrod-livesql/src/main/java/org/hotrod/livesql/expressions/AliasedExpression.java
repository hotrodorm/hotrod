package org.hotrod.livesql.expressions;

import java.util.List;
import java.util.logging.Logger;

import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.subqueries.Subquery;
import org.hotrod.livesql.queries.typesolver.TypeHandler;

public class AliasedExpression extends Expression {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(AliasedExpression.class.getName());

  private Expression referencedExpression;
  private String alias;

  public AliasedExpression(final Expression referencedExpression, final String alias) {
    super(referencedExpression.getPrecedence());
    this.referencedExpression = referencedExpression;
    this.alias = alias;
    super.register(this.referencedExpression);
  }

  protected Expression asSubqueryExpression(final Subquery subquery, final String alias) {
    return this.referencedExpression.asSubqueryExpression(subquery, alias);
  }

  @Override
  protected Expression getEmergingExpression() {
    Expression ee = this.referencedExpression.getEmergingExpression();
    return new AliasedExpression(ee, this.alias);
  }

  // TypeHandler setter

  public TypedExpression type(final Class<?> type) {
    return new TypedExpression(this, type);
  }

  @Override
  protected void setTypeHandler(TypeHandler typeHandler) {
    this.referencedExpression.setTypeHandler(typeHandler);
  }

  @Override
  protected TypeHandler<?,?> getTypeHandler() {
    return this.referencedExpression.getTypeHandler();
  }

  // Rendering

  @Override
  protected String getReferenceName() {
    return this.alias;
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    this.referencedExpression.renderTo(w);
    w.write(" as ");
    w.write(w.getSQLDialect().canonicalToNatural(this.alias));
  }

  @Override
  protected List<Expression> expand() {
    return referencedExpression.expand();
  }

  @Override
  protected String render() {
    return "'" + this.alias + "' for " + this.referencedExpression.toString();
  }

  public String toString() {
    return "'" + this.alias + "' for @" + String.format("%08x", System.identityHashCode(this.referencedExpression))
        + " " + this.referencedExpression.toString();

  }

}
