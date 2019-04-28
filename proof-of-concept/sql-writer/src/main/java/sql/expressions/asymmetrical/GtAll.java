package sql.expressions.asymmetrical;

import sql.ExecutableSelect;
import sql.expressions.Expression;

public class GtAll extends AsymmetricalOperator {

  public GtAll(final Expression value, final ExecutableSelect subquery) {
    super(value, "> all", subquery);
  }

}
