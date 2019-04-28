package sql.expressions.asymmetrical;

import sql.ExecutableSelect;
import sql.expressions.Expression;

public class NotIn extends AsymmetricalOperator {

  public NotIn(final Expression value, final ExecutableSelect subquery) {
    super(value, "not in", subquery);
  }

}
