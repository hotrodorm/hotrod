package org.hotrod.runtime.livesql.expressions.asymmetric;

import org.hotrod.runtime.livesql.ExecutableSelect;
import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public abstract class AsymmetricOperator extends Predicate {

  private static final int PRECEDENCE = 6;

  private Expression<?> value;
  private String operator;
  private ExecutableSelect subquery;

  protected AsymmetricOperator(final Expression<?> value, final String operator, final ExecutableSelect subquery) {
    super(PRECEDENCE);
    this.value = value;
    this.operator = operator;
    this.subquery = subquery;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    super.renderInner(this.value, w);
    w.write(" " + this.operator + " (\n");
    w.enterLevel();
    this.subquery.renderTo(w);
    w.exitLevel();
    w.write("\n)");
  }

}
