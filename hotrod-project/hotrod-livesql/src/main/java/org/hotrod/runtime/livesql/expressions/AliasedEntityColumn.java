package org.hotrod.runtime.livesql.expressions;

import java.util.List;
import java.util.logging.Logger;

import org.hotrod.runtime.livesql.queries.QueryWriter;

public class AliasedEntityColumn extends Expression {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(AliasedEntityColumn.class.getName());

  private Expression referencedExpression;
  private String alias;

  public AliasedEntityColumn(final Expression referencedExpression, final String alias) {
    super(referencedExpression);
    this.referencedExpression = referencedExpression;
    this.alias = alias;
    super.register(this.referencedExpression);
  }

  // Rendering

  protected String getReferenceName() {
    return this.alias;
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
