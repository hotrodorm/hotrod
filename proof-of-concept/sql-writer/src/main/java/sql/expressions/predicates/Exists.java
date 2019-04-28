package sql.expressions.predicates;

import sql.ExecutableSelect;
import sql.QueryWriter;

public class Exists extends Predicate {

  private static final int PRECEDENCE = 2;

  private ExecutableSelect subquery;

  public Exists(final ExecutableSelect subquery) {
    super(PRECEDENCE);
    this.subquery = subquery;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.write("exists (\n");
    this.subquery.renderTo(w);
    w.write("\n)");
  }

}
