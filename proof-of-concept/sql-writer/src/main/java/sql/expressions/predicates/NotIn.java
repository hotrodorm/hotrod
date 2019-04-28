package sql.expressions.predicates;

import sql.ExecutableSelect;
import sql.QueryWriter;
import sql.expressions.Expression;

public class NotIn extends Predicate {

  private static final int PRECEDENCE = 6;

  private Expression value;
  private ExecutableSelect subquery;

  public NotIn(final Expression value, final ExecutableSelect subquery) {
    super(PRECEDENCE);
    this.value = value;
    this.subquery = subquery;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    super.renderInner(this.value, w);
    w.write(" not in (\n");
    this.subquery.renderTo(w);
    w.write("\n)");
  }

}
