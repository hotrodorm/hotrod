package org.hotrod.livesql.expressions.character;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.util.CastUtil;

public class CharCast extends CharExpression {

  private Expression expr;
  private String type;

  public CharCast(Expression expr, String type) {
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
