package sql.expressions.asymmetrical;

import sql.ExecutableSelect;
import sql.expressions.Expression;

public class NeAll extends AsymmetricalOperator {

  public NeAll(final Expression value, final ExecutableSelect subquery) {
    super(value, "<> all", subquery);
  }

}
