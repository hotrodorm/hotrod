package sql.expressions.asymmetrical;

import sql.ExecutableSelect;
import sql.expressions.Expression;

public class EqAny extends AsymmetricalOperator {

  public EqAny(final Expression value, final ExecutableSelect subquery) {
    super(value, "= any", subquery);
  }

}
