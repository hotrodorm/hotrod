package sql.expressions.asymmetrical;

import sql.ExecutableSelect;
import sql.expressions.Expression;

public class NeAny extends AsymmetricalOperator {

  public NeAny(final Expression value, final ExecutableSelect subquery) {
    super(value, "<> any", subquery);
  }

}
