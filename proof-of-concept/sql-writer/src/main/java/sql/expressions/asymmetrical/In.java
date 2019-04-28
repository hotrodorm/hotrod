package sql.expressions.asymmetrical;

import sql.ExecutableSelect;
import sql.expressions.Expression;

public class In extends AsymmetricalOperator {

  public In(final Expression value, final ExecutableSelect subquery) {
    super(value, "in", subquery);
  }

}
