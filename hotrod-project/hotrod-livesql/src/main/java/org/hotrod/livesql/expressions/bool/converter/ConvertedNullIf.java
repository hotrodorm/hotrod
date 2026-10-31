package org.hotrod.livesql.expressions.bool.converter;

import java.util.logging.Logger;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.SortableExpression;
import org.hotrod.livesql.queries.ConvertedValue;
import org.hotrod.livesql.queries.QueryWriter;

public class ConvertedNullIf<R, D> extends SortableExpression {

  private static final Logger log = Logger.getLogger(ConvertedNullIf.class.getName());

  private ConvertedEntityColumn<R, D> c;
  private ConvertedValue v;

  public ConvertedNullIf(final ConvertedEntityColumn<R, D> c, final ConvertedValue v) {
    super(Expression.PRECEDENCE_FUNCTION);
    log.fine("init");
    this.c = c;
    this.v = v;
  }

  @Override
  protected void renderTo(QueryWriter w) {
    w.write("nullif(");
    this.c.renderTo(w);
    w.write(", ");
    this.v.renderTo(w);
    w.write(")");
  }

}
