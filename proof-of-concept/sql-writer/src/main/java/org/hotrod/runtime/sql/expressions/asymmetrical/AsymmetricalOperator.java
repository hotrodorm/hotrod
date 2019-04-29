package org.hotrod.runtime.sql.expressions.asymmetrical;

import org.hotrod.runtime.sql.ExecutableSelect;
import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.expressions.Expression;
import org.hotrod.runtime.sql.expressions.predicates.Predicate;

public abstract class AsymmetricalOperator extends Predicate {

  private static final int PRECEDENCE = 6;

  private Expression value;
  private String operator;
  private ExecutableSelect subquery;

  protected AsymmetricalOperator(final Expression value, final String operator, final ExecutableSelect subquery) {
    super(PRECEDENCE);
    this.value = value;
    this.operator = operator;
    this.subquery = subquery;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    super.renderInner(this.value, w);
    w.write(" " + this.operator + " (\n");
    this.subquery.renderTo(w);
    w.write("\n)");
  }

}
