package sql.expressions.predicates;

import sql.QueryWriter;
import sql.expressions.Expression;

public class IsNotNull extends Predicate {

  private static final int PRECEDENCE = 6;

  private Expression a;

  public IsNotNull(final Expression a) {
    super(PRECEDENCE);
    this.a = a;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    super.renderInner(this.a, w);
    w.write(" is not null");
  }

}