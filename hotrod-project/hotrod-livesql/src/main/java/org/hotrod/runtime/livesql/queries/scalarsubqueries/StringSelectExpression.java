package org.hotrod.runtime.livesql.queries.scalarsubqueries;

import org.hotrod.runtime.livesql.Row;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class StringSelectExpression extends StringExpression {

  // Properties

  protected AbstractSelect<Row> select;

  // Constructor

  public StringSelectExpression(final AbstractSelect<Row> select) {
    super(Expression.PRECEDENCE_PARENTHESIS);
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
