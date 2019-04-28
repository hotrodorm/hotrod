package sql.expressions.asymmetrical;

import sql.ExecutableSelect;
import sql.expressions.Expression;

public class LeAny extends AsymmetricalOperator {

  public LeAny(final Expression value, final ExecutableSelect subquery) {
    super(value, "<= any", subquery);
  }

}
