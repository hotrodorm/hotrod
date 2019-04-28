package sql.expressions.asymmetrical;

import sql.ExecutableSelect;
import sql.expressions.Expression;

public class LtAny extends AsymmetricalOperator {

  public LtAny(final Expression value, final ExecutableSelect subquery) {
    super(value, "< any", subquery);
  }

}
