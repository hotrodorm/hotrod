package org.hotrod.runtime.livesql.queries.scalarsubqueries;

import org.hotrod.runtime.livesql.Row;
import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.object.ObjectExpression;
import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject;

public class ObjectSelectExpression extends ObjectExpression {

  // Properties

  protected AbstractSelectObject<Row> select;

  // Constructor

  public ObjectSelectExpression(final AbstractSelectObject<Row> select) {
    super(Expression.PRECEDENCE_PARENTHESIS);

    if (select == null) {
      throw new LiveSQLException("Subquery select query cannot be null", null);
    }

    this.select = select;
  }

  // Rendering

  @Override
  public final void renderTo(final QueryWriter w) {
    w.enterLevel();
    w.write("(\n");
    this.select.renderTo(w);
    w.write(")");
    w.exitLevel();
  }

}