package sql.expressions.asymmetrical;

import sql.ExecutableSelect;
import sql.expressions.Expression;

public class EqAll extends AsymmetricalOperator {

  public EqAll(final Expression value, final ExecutableSelect subquery) {
    super(value, "= all", subquery);
  }

}
