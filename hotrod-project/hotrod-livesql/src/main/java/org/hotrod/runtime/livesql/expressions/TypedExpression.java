package org.hotrod.runtime.livesql.expressions;

import java.util.List;

import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.typesolver.TypeHandler;

public class TypedExpression extends Expression {

  private Expression expr;

  public TypedExpression(Expression expr, Class<?> type) {
    super(expr);
    super.setTypeHandler(TypeHandler.of(type));
    this.expr = expr;
  }

  protected String getReferenceName() {
    return this.expr.getReferenceName();
  }

  @Override
  protected void captureTypeHandler() {
    this.expr.captureTypeHandler();
    super.setTypeHandler(this.expr.getTypeHandler());
  }

  @Override
  protected void renderTo(QueryWriter w) {
    this.expr.renderTo(w);
  }

  protected List<Expression> unwrap() {
    return expr.unwrap();
  }

}
