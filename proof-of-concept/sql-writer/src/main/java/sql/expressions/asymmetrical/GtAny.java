package sql.expressions.asymmetrical;

import sql.ExecutableSelect;
import sql.expressions.Expression;

public class GtAny extends AsymmetricalOperator {

  public GtAny(final Expression value, final ExecutableSelect subquery) {
    super(value, "> any", subquery);
  }

}
