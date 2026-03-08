package org.hotrod.livesql.queries.scalarsubqueries;

import org.hotrod.dynamicsql.Row;
import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.bool.BooleanSyntaxExpression;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.select.FlatSelectObject;

public class BooleanSelectExpression extends BooleanSyntaxExpression {

  // Properties

  protected FlatSelectObject<Row> select;

  // Constructor

  public BooleanSelectExpression(final FlatSelectObject<Row> select) {
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
