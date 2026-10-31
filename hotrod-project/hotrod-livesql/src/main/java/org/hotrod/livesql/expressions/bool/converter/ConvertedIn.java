package org.hotrod.livesql.expressions.bool.converter;

import java.util.logging.Logger;

import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.bool.BooleanSyntaxExpression;
import org.hotrod.livesql.queries.ConvertedValue;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.utils.Separator;

public class ConvertedIn<R, D> extends BooleanSyntaxExpression {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(ConvertedIn.class.getName());

  private ConvertedEntityColumn<R, D> c;
  private ConvertedValue[] vs;

  public ConvertedIn(final ConvertedEntityColumn<R, D> c, final ConvertedValue[] vs) {
    super(Expression.PRECEDENCE_EQ_NE_LT_LE_GT_GE);
    this.c = c;
    if (vs.length == 0) {
      throw new LiveSQLException("The IN operator must use a non-empty list of values, but none was provided.");
    }
    this.vs = vs;
  }

  @Override
  protected void renderTo(QueryWriter w) {

    // 1. The column

    this.c.renderTo(w);

    // 2. Operator open

    w.write(" IN (");

    // 3. The converted values

    Separator sep = new Separator(", ");
    for (ConvertedValue v : this.vs) {
      w.write(sep.render());
      v.renderTo(w);
    }

    // 4. Operator close

    w.write(")");

  }

}
