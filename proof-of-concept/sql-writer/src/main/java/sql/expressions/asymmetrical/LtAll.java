package sql.expressions.asymmetrical;

import sql.ExecutableSelect;
import sql.expressions.Expression;

public class LtAll extends AsymmetricalOperator {

  public LtAll(final Expression value, final ExecutableSelect subquery) {
    super(value, "< all", subquery);
  }

}
