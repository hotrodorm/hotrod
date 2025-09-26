package org.hotrod.livesql.expressions.bool;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.util.CastUtil;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class BooleanCast extends Predicate {

  private Expression expr;
  private String type;

  public BooleanCast(Expression expr, String type) {
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
