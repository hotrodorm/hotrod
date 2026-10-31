package org.hotrod.livesql.expressions.bool.converter;

import java.util.logging.Logger;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.bool.BooleanSyntaxExpression;
import org.hotrod.livesql.queries.ConvertedValue;
import org.hotrod.livesql.queries.QueryWriter;

public class ConvertedEqual<R, D> extends BooleanSyntaxExpression {

  private static final Logger log = Logger.getLogger(ConvertedEqual.class.getName());

  private ConvertedEntityColumn<R, D> c;
  private ConvertedValue v;

  public ConvertedEqual(final ConvertedEntityColumn<R, D> c, final ConvertedValue v) {
    super(Expression.PRECEDENCE_EQ_NE_LT_LE_GT_GE);
    log.fine("init");
    this.c = c;
    this.v = v;
  }

  @Override
  protected void renderTo(QueryWriter w) {
    this.c.renderTo(w);
    w.write(" = ");
    this.v.renderTo(w);
  }

}
