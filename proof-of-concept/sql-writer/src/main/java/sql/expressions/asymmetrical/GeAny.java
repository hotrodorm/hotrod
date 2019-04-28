package sql.expressions.asymmetrical;

import sql.ExecutableSelect;
import sql.expressions.Expression;

public class GeAny extends AsymmetricalOperator {

  public GeAny(final Expression value, final ExecutableSelect subquery) {
    super(value, ">= any", subquery);
  }

}
