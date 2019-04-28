package sql.expressions.asymmetrical;

import sql.ExecutableSelect;
import sql.expressions.Expression;

public class LeAll extends AsymmetricalOperator {

  public LeAll(final Expression value, final ExecutableSelect subquery) {
    super(value, "<= all", subquery);
  }

}
