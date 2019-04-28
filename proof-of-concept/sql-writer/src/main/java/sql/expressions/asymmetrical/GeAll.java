package sql.expressions.asymmetrical;

import sql.ExecutableSelect;
import sql.expressions.Expression;

public class GeAll extends AsymmetricalOperator {

  public GeAll(final Expression value, final ExecutableSelect subquery) {
    super(value, ">= all", subquery);
  }

}
