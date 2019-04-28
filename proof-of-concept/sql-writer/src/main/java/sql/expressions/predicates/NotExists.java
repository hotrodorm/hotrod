package sql.expressions.predicates;

import sql.ExecutableSelect;
import sql.QueryWriter;

public class NotExists extends Predicate {

  private static final int PRECEDENCE = 2;

  private ExecutableSelect subquery;

  public NotExists(final ExecutableSelect subquery) {
    super(PRECEDENCE);
    this.subquery = subquery;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.write("not exists (\n");
    this.subquery.renderTo(w);
    w.write("\n)");
  }

}
