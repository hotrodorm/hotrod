package org.hotrod.runtime.livesql.expressions;

import java.util.List;
import java.util.logging.Logger;

import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.typesolver.TypeHandler;

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

  // TypeHandler setter

  public TypedExpression type(final Class<?> type) {
    super.setTypeHandler(TypeHandler.of(type));
    return new TypedExpression(this);
  }

  // Rendering

  protected String getReferenceName() {
    return this.alias;
  }

  @Override
  protected void captureTypeHandler() {
    this.referencedExpression.captureTypeHandler();
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

  protected String render() {
    return "'" + this.alias + "' for " + this.referencedExpression.toString();
  }

}
