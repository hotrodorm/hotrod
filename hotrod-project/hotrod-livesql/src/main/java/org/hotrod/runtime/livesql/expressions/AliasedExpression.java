package org.hotrod.runtime.livesql.expressions;

import java.util.logging.Logger;

import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.ReferenceableExpression;
import org.hotrodorm.hotrod.utils.TUtil;

public class AliasedExpression extends Expression implements ReferenceableExpression {

  private static final Logger log = Logger.getLogger(AliasedExpression.class.getName());

  private Expression referencedExpression;
  private String alias;

  public AliasedExpression(final Expression referencedExpression, final String alias) {
    super(PRECEDENCE_ALIAS);
    this.referencedExpression = referencedExpression;
    super.register(this.referencedExpression);
    this.alias = alias;
  }

  @Override
  protected void computeQueryColumns() {
    log.info("@@@@@@@@@@@@@@@@@ '" + this.alias + "' -- this.referencedExpression: "
        + this.referencedExpression.getClass().getName());
    log.info("@@@ " + TUtil.compactStackTrace());
    this.referencedExpression.computeQueryColumns();
    this.setTypeHandler(this.referencedExpression.getTypeHandler());
  }

  @Override
  public void renderTo(final QueryWriter w) {
    this.referencedExpression.renderTo(w);
    w.write(" as ");
    w.write(w.getSQLDialect().canonicalToNatural(this.alias));
  }

  @Override
  public String getName() {
    return this.alias;
  }

  protected Expression getExpression() {
    return referencedExpression;
  }

}
