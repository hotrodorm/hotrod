package org.hotrod.runtime.livesql.expressions;

import java.util.List;

import org.hotrod.runtime.livesql.queries.QueryWriter;

public class TypedExpression extends Expression {

  private Expression expr;

  public TypedExpression(Expression expr) {
    super(expr.getPrecedence());
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
