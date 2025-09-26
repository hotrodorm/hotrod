package org.hotrod.livesql.expressions.numeric;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.util.CastUtil;

public class NumericCast extends NumericExpression {

  private Expression expr;
  private String type;

  public NumericCast(Expression expr, String type) {
    super(Expression.PRECEDENCE_FUNCTION);
    CastUtil.validateCastType(type);
    this.expr = expr;
    this.type = type;
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getCastRenderer().render(w, this.expr, this.type);
  }

}
